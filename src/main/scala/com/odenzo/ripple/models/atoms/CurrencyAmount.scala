package com.odenzo.ripple.models.atoms

import java.text.DecimalFormat

import cats._
import cats.implicits._
import io.circe._
import io.circe.syntax._

/** CurrencyAmount is FiatAmount  or XRP Drops in all cases.
  * Some documentation refers to CurrencyAmount or Currency w/o Amount. The latter is now poorly modeled
  * by Script.
  */
sealed trait CurrencyAmount

final case class FiatAmount(amount: BigDecimal, script: Script) extends CurrencyAmount {
  def abs: FiatAmount    = this.copy(amount = amount.abs)
  def negate: FiatAmount = this.copy(amount = -amount)
}

/**
  * Monetary amount with a given script. Script has to be an issuer, not XRP.
  * So there is no empty to define a monoid but can define a semigroup
  *
  */
object FiatAmount {

  /* Note this doesn't show the issuer */
  implicit val show: Show[FiatAmount] = Show.show[FiatAmount] { v =>
    format(v.amount) + " " + v.script.show
  }

  /** Could derive and then merge, not sure other ways in latest circe. Encode Script and add field? */
  implicit val encoder: Encoder.AsObject[FiatAmount] = Encoder.AsObject.instance { fa: FiatAmount =>
    JsonObject(
      "value"    -> fa.amount.toString().asJson, // Need to avoid funny formatting
      "currency" -> fa.script.currency.asJson,
      "issuer"   -> fa.script.issuer.asJson
    )
  }

  implicit val decode: Decoder[FiatAmount] = new Decoder[FiatAmount] {
    final def apply(c: HCursor): Decoder.Result[FiatAmount] = {
      (c.get[BigDecimal]("value"), c.as[Script]).mapN(FiatAmount.apply)
    }
  }

  /** Creates a formatter and applied, multithread safe */
  def format(bigDecimal: BigDecimal): String = new DecimalFormat("#,##0.00").format(bigDecimal)
}

/**
  *
  * Maximum number of drops is bigger than long. This allows negative values, but rarely used
  *  e.g. ripple_path_find
  */
final case class Drops(amount: BigInt) extends CurrencyAmount {
  def mult(m: BigInt) = Drops(amount * m)
}

object CurrencyAmount {

  // Compiler warns this would fail on FiatAmount ? WTF, with partial or full match style.
  // Maybe this is the FiatAmount object
  implicit val encoder: Encoder[CurrencyAmount] = Encoder.instance[CurrencyAmount] {
    case v: FiatAmount => v.asJson
    case v: Drops      => v.asJson
  }

  implicit val decoder: Decoder[CurrencyAmount] = {
    val wideDrops: Decoder[CurrencyAmount] = Decoder[Drops].map(v => v: CurrencyAmount)
    val wideFiat: Decoder[CurrencyAmount]  = Decoder[FiatAmount].map(v => v: CurrencyAmount)
    wideDrops.or(wideFiat)
  }

  implicit val show: Show[CurrencyAmount] = Show.show {
    case v: FiatAmount => v.show
    case v: Drops      => v.show
  }
}

/**
  * A Drop is always XRP, one millionth of an XRP to be exact. This is the smallest XRP unit and will always be integer number.
  * We always use Drops internally. Special formatting for human display TBD
  * Should make a Monoid for the sake of it, actually because Drops.empty is nice and so is CombineAll
  * Also note there are implicit conversion from long to BigDecimal?
  */
object Drops {

  final val zero: Drops   = Drops(0)
  final val stdFee: Drops = Drops(10)

  implicit val show: Show[Drops] = Show.show[Drops] { v =>
    format(v) + "[Drops]" // May want to format with ,
  }

  // So, those Drops. Mostly in Strings, EXCEPT the LedgerNodes (FeeSettingsNode in particular)
  // Might be wrong about that, except for ReferenceFeeUnits
  private val stringDrops = Decoder.decodeString.map(rs => Drops(BigInt(rs)))
  private val longDrops   = Decoder.decodeLong.map(l => Drops(BigInt(l)))

  // REgression bug or are these always longs now. They are in subscribe messages. Bastards
  implicit val encoder: Encoder[Drops] = Encoder.encodeString.contramap[Drops](d => d.amount.toString())
  implicit val decoder: Decoder[Drops] = stringDrops.or(longDrops)

  /**
    * Another type class instance. SemiGroup
    */
  implicit val monoid: Monoid[Drops] = new Monoid[Drops] {
    override def empty: Drops = Drops.zero

    override def combine(x: Drops, y: Drops): Drops = Drops(x.amount + y.amount)
  }

  private val dropsPerXRP = BigDecimal(1000000L)

  def apply(amount: Long): Drops = Drops(BigInt(amount))

  def fromXrp(l: Long): Drops = Drops((BigDecimal(l) * dropsPerXRP).toBigInt)

  def fromXrp(s: String): Drops = Drops((BigDecimal(s) * dropsPerXRP).toBigInt)
  // COuld do with a Cats map function here (Functor or more for fun. We have a zero, we have a combine (addition),
  // we have a map..so a monoid would see appropriate

  /** Formats integral with commas and NO CURRENCY LABEL */
  def format(drops: Drops): String = new DecimalFormat("#,##0").format(drops.amount)

  def formatAsXrp(drops: Drops): String = {
    val xrpAmt: BigDecimal = BigDecimal.exact(drops.amount) / dropsPerXRP
    new DecimalFormat("#,##0").format(xrpAmt)
  }
}
