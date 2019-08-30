package com.odenzo.ripple.models.atoms

import java.time.DateTimeException
import scala.util.Try

import cats._
import cats.data._
import cats.implicits._
import cats.implicits._
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.syntax._

import com.odenzo.ripple.models.utils.caterrors.{AppError, AppException}

/**
  * TODO: Redo, and reference other Ledger type things, like LastLedgerIndex
  * Representations of a Ledger 'handle'
  * For instance, in ripple commands ledger_hash or ledger_index fields can be used to identify ledger to work with.
  *  Hash is unique, ledger_index can be either  a named ledger (String) such as "Validated" or an unsigned int value
  *  (a real index)
  *
  *
  *  This whole hack depends on case classes having  "... ledger:Ledger".
  *  The encoding to Json then checks is Ledger is LedgerHash and encoded "ledger_hash" = "theHash"
  *  or if LedgerIndex uses "ledger_index" field with either String in unsigned number value depending
  *  on if LedgerName or LedgerSequence.
  *  https://xrpl.org/basic-data-types.html#specifying-ledgers
  *  See AccountCurrenciesRq and AccountCurrenciesRs for example of using this type of encoding
  *  in the Circe Encoder and Decoders.
  */
sealed trait LedgerID

/** Represents a value that identifies a ledger. Encoded as ledger_index fields, long or string */
sealed trait LedgerIndex extends LedgerID

/** Ledger Hash is encoded as leger_hash field in API */
case class LedgerHash(v: RippleHash) extends LedgerID

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

  /** This encoder will create a Json object for ledger. But we always need to lift those objects up a level
    * Might as well make keys for all to make it easier to list.
    **/
  implicit val encoder: Encoder[LedgerID] = Encoder.instance[LedgerID] {
    case l @ LedgerName(n)     => Encoder[LedgerName].apply(l)
    case l @ LedgerSequence(n) => Encoder[LedgerSequence].apply(l)
    case l @ LedgerHash(n)     => Encoder[LedgerHash].apply(l)

  }

  /**
    * Takes a ledger handle type from either ledger_hash or ledger_index.
    * If ledger_hash can be parse returns that, else tries for ledger_index (returning that error if fails)
    */
  implicit val decoder: Decoder[LedgerID] =
    Decoder.decodeEither[LedgerHash, LedgerIndex]("ledger_hash", "ledger_index").map(et => et.fold(identity, identity))

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
  val CURRENT_LEDGER: LedgerName   = LedgerName("current")
  val VALIDATED_LEDGER: LedgerName = LedgerName("validated")
  val CLOSED_LEDGER: LedgerName    = LedgerName("closed")
  import io.circe._
  import io.circe.generic.extras.semiauto._

  // Better to have this an enumeration eventually

  implicit val decoder: Codec[LedgerName] = deriveUnwrappedCodec[LedgerName]
}

import io.circe._
import io.circe.syntax._
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
  implicit val codec: Codec[LedgerHash] = deriveUnwrappedCodec[LedgerHash]

}
