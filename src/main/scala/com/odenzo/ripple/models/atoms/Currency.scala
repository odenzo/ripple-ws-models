package com.odenzo.ripple.models.atoms

import cats._
import cats.implicits._
import io.circe.Decoder.Result
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.support.AccountZero

case class Currency(symbol: String)

object Currency {

  // val tfFulleCanonicalSig = TxnFlag(0x80000000, "Txn Security Flag")
  /** Use explicitly included paths only */
  val XRP = Currency("XRP")

  val BTC = Currency("BTC")

  val USD = Currency("USD")

  val NZD = Currency("NZD")

  val AUD = Currency("AUD")

  val EUR = Currency("EUR")

  val THB = Currency("THB")

  val ETH = Currency("ETH")

  implicit val codec: Codec[Currency] = deriveUnwrappedCodec[Currency]
  implicit val show: Show[Currency]   = Show.show[Currency](c => s"[${c.symbol}]")
}

/**
  *  In many places a Script can be XRP (e.g. BookOfferRq). XRP Script has no AccountAddr and is encoded differently.
  *   We use AccountZero to denote the issuer, but the issuer is not encoded.
  * @param currency  Standard currency code, CANNOT BE XRP though.
  * @param issuer The account that issues the currency
  */
case class Script(currency: Currency, issuer: AccountAddr)

object Script {

  val XRP = Script(Currency.XRP, AccountZero.address)

  implicit val show: Show[Script] = Show.show[Script](v => v.currency.show + s" Issuer: ${v.issuer.show}")
  implicit val encoder: Encoder.AsObject[Script] = Encoder.AsObject.instance[Script] {
    case Script(Currency.XRP, _) => JsonObject.singleton("currency", "XRP".asJson)
    case normal                  => JsonObject("currency" := normal.currency, "issuer" := normal.issuer)
  }
  implicit val decoder: Decoder[Script] = Decoder.instance { hc =>
    for {
      currency <- hc.get[Currency]("currency")
      issuer   <- hc.get[Option[AccountAddr]]("issuer")
      result <- (currency, issuer) match {
        case (Currency.XRP, None)    => Script.XRP.asRight
        case (Currency.XRP, Some(i)) => DecodingFailure(s"XRP with Issuer $i!", hc.history).asLeft
        case (c, Some(i))            => Script(c, i).asRight
        case (c, None)               => DecodingFailure(s"Non-XRP $c with No Issuer!", hc.history).asLeft
      }
    } yield result
  }

}
