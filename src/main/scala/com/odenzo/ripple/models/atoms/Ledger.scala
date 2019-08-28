package com.odenzo.ripple.models.atoms

import java.time.DateTimeException
import scala.util.Try

import cats._
import cats.data._
import cats.implicits._
import cats.implicits._
import io.circe.Decoder.Result
import io.circe._
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
sealed trait Ledger

/** Represents a value that identifies a ledger. Encoded as ledger_index fields, long or string */
sealed trait LedgerIndex extends Ledger

/** Ledger Hash is encoded as leger_hash field in API */
case class LedgerHash(v: RippleHash) extends Ledger

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

object Ledger {

  /** FIXME: Dodgy unsafe hack that relies on unvalidated Hash being a valid hash to discern what type
    *   The ledger field can either be a hash or an index. THe name in the Json Object for the field is
    *   dependant on that type. If a number we rename field to ledger_index.
    *   P
    *   If the fieldName in the JsonObject is present, we assume its iether a valid ledger hash or a ledger index.
    *   If a number then always a ledger index. If a String and a valid hash treat as ledger_hash else ledger_index.
    *   Basically, all this is to set the correct name of the json field.
    *
    * @param autoLedger JSON Object.
    * @param fieldName Field to probe the value, and rename the key based on the value (index number of hash)
    * @return
    */
  def renameLedgerField(autoLedger: JsonObject, fieldName: String = "ledger"): JsonObject = {
    val oldKey = fieldName

    // Looks like Json.fold is reasonable way but not now.
    // Also, case Json.JString(v)  not working really. Are there unaaply somewhere.
    val ledgerVal: Option[(String, Json)] = autoLedger(oldKey).map {
      case json if json.isNumber => ("ledger_index", json)
      case json if json.isString =>
        val hashOrName: (String, Json) = json.asString match {
          case Some(ledger) if Hash256.isValidHash(ledger) => ("ledger_hash", json)
          case Some(assume_named_ledger)                   => ("ledger_index", json)
        }
        hashOrName

      case json if json.isNull => ("ledger_index", json)        // Where is will be stripped again ^_^
      case other               => ("INVALID_LEDGER", Json.Null) // Not sure how to signal error yet
    }

    ledgerVal.map(field => field +: autoLedger.remove(oldKey)).getOrElse(autoLedger)

  }

  /**
    * Generic lifter being applied just to ledger default encoding for now. The default encoder will
    * make a Ledger subobject in Json, with differing fields based on the concrete instance Ledger subtype.
    * (i.e LedgerIndex, LedgerHash, LedgerId, LedgerName...)
    * @param withLedger
    * @param field
    * @return
    */
  def liftLedgerFields(withLedger: JsonObject, field: String = "ledger"): JsonObject = {

    val replaced: Option[JsonObject] = withLedger(field).map { subs =>
      // We should make this subs is a JsonObject to be pedantic and give good error messages
      val cursor = subs.hcursor
      // Lets make this more generic for fun
      val fieldsToLift: List[String] = cursor.keys.getOrElse(Vector.empty[String]).toList
      // val expectedFields: List[String] = "ledger_hash" :: "ledger_index" :: Nil

      // product should do the trick, or need OptionT?
      val fields: List[(String, Json)] = fieldsToLift.flatMap(name => cursor.downField(name).focus.map((name, _)))

      val newBase = withLedger.toList.filterNot(f => f._1.equals(field)) ::: fields
      JsonObject.fromIterable(newBase)
    }
    // If we found an object named field then we took all the subfield (possibly none) and shifted to field in new
    // JsonObject. If no field was found then we return the origiinal
    // TODO: Refactor (getOrElse {...} on top matches human description more
    replaced.getOrElse(withLedger)
  }

  /** This encoder will create a Json object for ledger. But we always need to lift those objects up a level
    * Might as well make keys for all to make it easier to list.
    **/
  implicit val encoder: Encoder[Ledger] = Encoder.instance[Ledger] {
    case l @ LedgerName(n)     => Encoder[LedgerName].apply(l)
    case l @ LedgerSequence(n) => Encoder[LedgerSequence].apply(l)
    case l @ LedgerHash(n)     => Encoder[LedgerHash].apply(l)

  }

  /**
    * Takes a ledger handle type from either ledger_hash or ledger_index.
    * If ledger_hash can be parse returns that, else tries for ledger_index (returning that error if fails)
    */
  implicit val decoder: Decoder[Ledger] = Decoder.instance[Ledger] { hc =>
    hc.get[LedgerHash]("ledger_hash") match {
      case ok @ Right(hash) => ok
      case Left(_)          => hc.get[LedgerIndex]("ledger_index")
    }
  }

}

object LedgerIndex {

  /** For encoding a ledger id as a field in an object */
  def toField(id: LedgerIndex): (String, Json) = ("ledger_index", encoder.apply(id))

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

  implicit val encoder: Encoder[LedgerName] = Encoder.encodeString.contramap[LedgerName](_.v)
  implicit val decoder: Decoder[LedgerName] = Decoder.decodeString.map(v => LedgerName(v))
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
  // FIXME: Hashes Hacked together
  def toField(hash: LedgerHash): (String, Json) = ("ledger_hash", encoder.apply(hash))
  implicit val encoder: Encoder[LedgerHash]     = Encoder[RippleHash].contramap[LedgerHash](v => v.v)
  implicit val decoder: Decoder[LedgerHash]     = Decoder.decodeString.map(s => LedgerHash(RippleHash(s)))

}
