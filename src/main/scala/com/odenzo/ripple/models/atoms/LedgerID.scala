package com.odenzo.ripple.models.atoms

import cats._
import cats.data._
import cats.implicits._
import io.circe._

/**
  * A LedgerID is either LedgerHash, LedgerName, LedgerSequence.
  * Each of this is represented in JsonObject as a simple field.
  * The field name varies: LedgerHash => "ledger_hash" while LedgerName and LedgerSequence go to "ledger_index"
  *
  * So:
  * - LedgerName : LedgerIndex : LedgerID
  * - LedgerSequence : LedgerIndex : LedgerID
  * - LedgerHash : LedgerID
  *
  *
  */
sealed trait LedgerID

/** Represents a value that identifies a ledger. Encoded as ledger_index fields, long or string */
sealed trait LedgerIndex extends LedgerID

/** Ledger Hash is encoded as leger_hash field in API, ledger hashses are 32 bytes in hex */
case class LedgerHash(v: String) extends LedgerID

/** Name of a ledger, e.g. "validated"  Could be an enumeration almost. The ledger specified
  * over time changes. So -- this should be changed to extend Ledger not LedgerId */
case class LedgerName(v: String) extends LedgerIndex

/** Represents a Ledger in Ripple via a Long value. May need to be BigInt eventually! */
case class LedgerSequence(v: Long) extends LedgerIndex {
  def plus(deltaIndex: Long): LedgerSequence  = this.copy(v = v + deltaIndex)
  def isAfter(other: LedgerSequence): Boolean = this.v > other.v
}

/** When doing inquiry on a "Current" ledger (which I seldom do) it returns ledger_current_index
  * and NOT ledger_index or ledger_hash
  *  This is a specific ledger at time of call, and just a hack. The json its located in on a current ledger
  *  has an index but not a hash (and the field name is different)
  * @param v  Ledger index value (unsigned)
  */
case class LedgerCurrentIndex(v: Long)

object LedgerCurrentIndex {

  import io.circe.generic.extras.semiauto._
  implicit val codec: Codec[LedgerCurrentIndex] = deriveUnwrappedCodec[LedgerCurrentIndex]

}

object LedgerID {
  import io.circe._
  import io.circe.syntax._

  def asJObjField(lid: LedgerID): (String, Json) = {
    lid match {
      case l @ LedgerName(_)     => "ledger_index" := l
      case l @ LedgerSequence(_) => "ledger_index" := l
      case l @ LedgerHash(_)     => "ledger_hash"  := l
    }
  }
  // Skipping object style because a trap, if using Leaf type totally different encoding than LedgerID

  def apply(s: String): LedgerID    = if (s.length != 32 * 2) LedgerName(s) else LedgerHash(s)
  def apply(seq: Long): LedgerIndex = LedgerSequence(seq)

  implicit val encoder: Encoder[LedgerID] = Encoder.instance[LedgerID] {
    case l: LedgerName     => Encoder[LedgerName].apply(l)
    case l: LedgerSequence => Encoder[LedgerSequence].apply(l)
    case l: LedgerHash     => Encoder[LedgerHash].apply(l)
  }

  // We have no discriminator field in the JSON so have to try most restrictive to least restricive
  implicit val decoder: Decoder[LedgerID] = {
    val d1 = Decoder[LedgerHash].widen[LedgerID]
    val d2 = Decoder[LedgerIndex].widen[LedgerID]
    d1 or d2
  }

  /** Decided to be principled. "field" : [LedgerHash|LedgerName|LedferSequence] are implicit.
    * When we have  { "ldeger_indexx" : ...  OR "ledger_hash" can use this a product.
    */
  val objEncoder: Encoder.AsObject[LedgerID] =
    Encoder.encodeEither[LedgerHash, LedgerIndex]("ledger_hash", "ledger_index").contramapObject[LedgerID] {
      case l: LedgerHash  => Left(l)
      case l: LedgerIndex => Right(l)
    }

  /**
    * Takes a ledger handle type from either ledger_hash or ledger_index.
    * If ledger_hash can be parse returns that, else tries for ledger_index (returning that error if fails)
    */
  val objDecoder: Decoder[LedgerID] =
    Decoder
      .decodeEither[LedgerHash, LedgerIndex]("ledger_hash", "ledger_index")
      .map(et => et.fold(identity, identity))

}

object LedgerIndex {

  /** These both get encoded to ledger_index field.  */
  implicit val encoder: Encoder[LedgerIndex] = Encoder.instance[LedgerIndex] {
    case LedgerName(n)     => Json.fromString(n)
    case LedgerSequence(x) => Json.fromLong(x)
  }

  private val failedDecoder = Decoder.failedWithMessage("LedgerID value $other was not String or Number")

  implicit val decoder: Decoder[LedgerIndex] = Decoder.instance[LedgerIndex] { hc =>
    hc.value match {
      case x if x.isNumber => Decoder[LedgerSequence].apply(hc)
      case x if x.isString => Decoder[LedgerName].apply(hc)
      case other           => failedDecoder.apply(hc)
    }
  }
}

object LedgerName {
  // Use enumeration Circe Style.
  val CURRENT_LEDGER: LedgerName   = LedgerName("current")
  val VALIDATED_LEDGER: LedgerName = LedgerName("validated")
  val CLOSED_LEDGER: LedgerName    = LedgerName("closed")
  import io.circe._
  import io.circe.generic.extras.semiauto._

  // Better to have this an enumeration eventually

  implicit val decoder: Codec[LedgerName] = deriveUnwrappedCodec[LedgerName]
}

import io.circe._
import io.circe.generic.extras.semiauto._

/** This is really an unsigned in in Ripple */
object LedgerSequence {
  implicit val codec: Codec[LedgerSequence] = deriveUnwrappedCodec[LedgerSequence]

  /** This is used in Ripple API to let the Ripple service choose most appropriate ledger
    * e.g. account_tx as min or max will choose the earliest/latest validated ledgers.
    */
  val WILDCARD_LEDGER: LedgerSequence = LedgerSequence(-1)
  val MAX: LedgerSequence             = LedgerSequence(4294967295L)
  val MIN: LedgerSequence             = LedgerSequence(0L)

}

/**
  * LedgerHash gets encoded to ledger_hash field
  */
object LedgerHash {
  val codec: Codec[LedgerHash]              = deriveUnwrappedCodec[LedgerHash]
  implicit val encoder: Encoder[LedgerHash] = codec
  implicit val decoder: Decoder[LedgerHash] = codec.ensure(lh => lh.v.length === 32 * 2, "LedgerHash != 20 bytes")
}
