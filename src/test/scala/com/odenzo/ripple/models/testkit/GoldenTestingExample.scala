package com.odenzo.ripple.models.testkit

import java.awt.JobAttributes.DestinationType

import cats._
import cats.data._
import cats.implicits._
import cats.kernel.Eq
import io.circe.testing.golden.GoldenCodecTests
import io.circe.testing.{ArbitraryInstances, CodecTests}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.flatspec.AnyFlatSpec
import org.typelevel.discipline.scalatest.Discipline

import com.odenzo.ripple.models.atoms.{AccountAddr, DestinationTag, Drops, InvoiceHash}
import com.odenzo.ripple.models.wireprotocol.txns.PaymentTx

/** An attempt to try out Golden testing which checks that Codec obeys Laws and doesn't break example message
  * See: https://github.com/circe/circe-golden */
trait PaymentTxTestInstances extends ArbitraryInstances {

  // Lots of work :-/  Probably should make Gen for common types at least, but even then diffucult
  // to ensure things like Address are valid etc.

  // Base58 Gen Needed,

  lazy implicit val arbitrary: Arbitrary[AccountAddr] = {
    val gen = Gen.listOfN(25, Gen.alphaChar).map(cs => AccountAddr(('r' :: cs).mkString))
    Arbitrary(gen)
  }

  implicit val eqVisit: Eq[PaymentTx] = Eq.fromUniversalEquals
  implicit val arbitraryVisit: Arbitrary[PaymentTx] = Arbitrary(
    for {
      addr      <- Arbitrary.arbitrary[AccountAddr]
      amount    <- Arbitrary.arbitrary[Long].map(i => Drops(i.abs))
      dest      <- Arbitrary.arbitrary[AccountAddr]
      dtag      <- Arbitrary.arbitrary[Option[Long]].map(_.map(DestinationTag.fromLong))
      invoiceID <- Gen.listOfN(256 / 4, Gen.hexChar).map(_.mkString).map(s => InvoiceHash(s).some)
    } yield PaymentTx(addr, amount, dest, dtag, invoiceID)
  )
}

// Verify we round trip at least.
class PaymentTxSuite extends AnyFlatSpec with Discipline with PaymentTxTestInstances {
  checkAll("Codec[PaymentTx]", CodecTests[PaymentTx].codec)
}

// Verify we round trip at least.
class PaymentTxGoldenSuite extends AnyFlatSpec with Discipline with PaymentTxTestInstances {
  checkAll("GoldenCodec[PaymentTx]", GoldenCodecTests[PaymentTx].goldenCodec)
}
