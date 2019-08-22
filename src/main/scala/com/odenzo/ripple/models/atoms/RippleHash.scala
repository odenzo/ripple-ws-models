package com.odenzo.ripple.models.atoms

import scala.util.Try

import cats.implicits._
import io.circe.{Decoder, Encoder}

/*
 * Note: There are also Hash160 and Hash128.
 * Haven't modelled those correctly ?
 *
 * These Hashes are still a bit of a mess.
 * These are really SHA-512Half hashes
 * Exactly 64 characters in length
 * Hexadecimal character set: 0-9 and A-F.
 * Typically written in upper case.
 */

sealed trait Hash256 {
  def v: String
}

object Hash256 {
  def validate(h: Hash256): Unit = require(isValidHash(h.v), "Ripple Hash Code must be exactly 64 chars")

  def isValid(h: Hash256): Boolean = isValidHash(h.v)

  /** Very crude Hash validation. Not actually used too much yet */
  def isValidHash(h: String): Boolean = (h.length === 64) && h.forall(isHexDigit)

  def isHexDigit(c: Char): Boolean = {
    // TODO: Not sure safe to use Java calls directly when migrating to ScalaJS
    Try { Character.digit(c, 16) }.toOption.fold(false)(i => true)
  }

  lazy val empty            = RippleHash("FaceBeef" * 8)
  lazy val unit: RippleHash = empty

  type AccountTxID = TxnHash

  implicit val encoder: Encoder[Hash256] = Encoder.encodeString.contramap[Hash256](_.v)
  implicit val decoder: Decoder[Hash256] = Decoder.decodeString.map(RippleHash(_))
}

case class PaymentChannelHash(v: String) extends Hash256
object PaymentChannelHash {
  implicit val encoder: Encoder[PaymentChannelHash] = Encoder.encodeString.contramap[PaymentChannelHash](_.v)
  implicit val decoder: Decoder[PaymentChannelHash] = Decoder.decodeString.map(PaymentChannelHash(_))
}

// LedgerHash over in Ledger file :-)

/**
  * Represents basic datatype for Ripple Hashes. There are a few types.
  * This is my poor design. Outside this file can embedd RippleHash
  * Really is generic hashses, and LedgerHash embedds is, but TxnHash extends Hash256 directly.
  * meh.... warts and all for now.
  * @see  [[https://ripple.com/build/rippled-apis/#hashes]]
  */
case class RippleHash(v: String) extends Hash256 {

  // All Hexadecimal characters not checked
}

/** A Hash for a Transaction that can be used for TxRq to lookup Transaction
  * e.g. 5DE3F4F3558EA2A278576E6E59ABA88A697A3BE3C97E017D590743D88F366C4D
  * */
case class TxnHash(v: String) extends Hash256

/**
  * Invoice Hash is a specific field on each transaction, similar to a memo fields.
  * Used for chargebacks and reversals as pattern at semantic layer.
  * Don't see anywhere to search by this, which would be nice.
  * @param v
  */
case class InvoiceHash(v: String) extends Hash256

/** For Payment Channels */
case class ChannelIndex(v: String) extends Hash256

/** TODO: Check on this, is it really AccountLedger ID */
case class AccountHash(v: String) extends Hash256

object RippleHash {
  implicit val encoder: Encoder[RippleHash] = Encoder.encodeString.contramap[RippleHash](_.v)
  implicit val decoder: Decoder[RippleHash] = Decoder.decodeString.map(RippleHash(_))

  /** For testing ^_^ */
  lazy val dummy = RippleHash("HashTest" * 8)

}

object TxnHash {
  implicit val encoder: Encoder[TxnHash] = Encoder.encodeString.contramap[TxnHash](_.v)
  implicit val decoder: Decoder[TxnHash] = Decoder.decodeString.map(TxnHash(_))

}

object ChannelIndex {
  implicit val encoder: Encoder[ChannelIndex] = Encoder.encodeString.contramap[ChannelIndex](_.v)
  implicit val decoder: Decoder[ChannelIndex] = Decoder.decodeString.map(ChannelIndex(_))
}

object AccountHash {
  implicit val encoder: Encoder[AccountHash] = Encoder.encodeString.contramap[AccountHash](_.v)
  implicit val decoder: Decoder[AccountHash] = Decoder.decodeString.map(v => AccountHash(v))
}

object InvoiceHash {
  implicit val encoder: Encoder[InvoiceHash] = Encoder.encodeString.contramap[InvoiceHash](_.v)
  implicit val decoder: Decoder[InvoiceHash] = Decoder.decodeString.map(InvoiceHash(_))

  val empty: InvoiceHash = InvoiceHash("0" * 64) // Correct number, define on Hash256 FIXME
}
