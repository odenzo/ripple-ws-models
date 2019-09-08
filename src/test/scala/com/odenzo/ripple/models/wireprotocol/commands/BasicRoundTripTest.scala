package com.odenzo.ripple.models.wireprotocol.commands

import io.circe.{Codec, Json}

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.GenesisAccount
import com.odenzo.ripple.models.testkit.CodecTesting
import cats._
import cats.data._
import cats.implicits._

import com.odenzo.ripple.models.utils.caterrors.AppJsonDecodingError
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo._
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.ledgerinfo._
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.pathandorderbook.{BookOffersRq, RipplePathFindRq}

/** Basic Roundtrip Tests for the Commands  */
class BasicRoundTripTest extends CodecTesting {

  val sampleAddr   = GenesisAccount.address
  val sampleDrops  = Drops(12)
  val sampleScript = Script(Currency("NZD"), sampleAddr)
  val sampleFiat   = FiatAmount(BigDecimal(22), sampleScript)

  def testForward[T <: RippleRq](rq: T)(implicit codec: Codec.AsObject[T]): Unit = {
    import io.circe.syntax._
    val json                                         = rq.asJson
    val jObj                                         = rq.asJsonObject
    val res: Either[AppJsonDecodingError, (T, Json)] = objRoundTrip(rq)

    res match {
      case Right((obj, j: Json)) =>
        logger.debug(s"\nObject:\n ${pprint.apply(obj)}\nJSON:\n${json.spaces4}")
        obj shouldEqual rq
      case Left(err) =>
        logger.error(s"RoundTrip Failed on $rq \n Details:\n ${err.show}")
        fail(s"RoundTrip Codec on $rq")
    }

  }

  test("AccountInfo Codecs") {
    val anAddress = GenesisAccount.address

    testForward(AccountChannelsRq(anAddress, None))
    testForward(AccountCurrenciesRq(anAddress))
    testForward(AccountInfoRq(anAddress, false, true))
    testForward(AccountLinesRq(anAddress, None))
    testForward(AccountLinesRq(anAddress, anAddress.some))
    testForward(AccountObjectsRq(anAddress, tipe = "state".some))
    testForward(AccountOffersRq(anAddress))
    testForward(AccountTxRq(anAddress, LedgerSequence(-1).some, LedgerSequence(555).some))
    testForward(GatewayBalancesRq(anAddress, hotwallet  = Seq(anAddress, anAddress))) // Nonsense to test
    testForward(NoRippleCheckRq(anAddress, transactions = true))

  }

  test("LedgerMethods Codecs") {
    testForward(LedgerRq(transactions = true))
    testForward(LedgerClosedRq())
    testForward(LedgerCurrentRq())
    testForward(LedgerDataRq())
    testForward(LedgerEntryRq(tipe = "state"))
    testForward(LedgerEntryRq(tipe = "state", account_root = sampleAddr.some))
  }

  test("PathAndOrderBook Codecs") {
    testForward(BookOffersRq(taker = sampleAddr.some, taker_gets = sampleScript, taker_pays = sampleScript))
    testForward(RipplePathFindRq(sampleAddr, sampleAddr, sampleDrops, None, None))
    testForward(RipplePathFindRq(sampleAddr, sampleAddr, sampleFiat, None, None))
  }

}
