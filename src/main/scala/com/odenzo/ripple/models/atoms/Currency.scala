package com.odenzo.ripple.models.atoms

import cats._
import cats.implicits._
import io.circe._
import io.circe.generic.semiauto._

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

  implicit val encoder: Encoder[Currency] = Encoder.encodeString.contramap[Currency](_.symbol)
  implicit val decoder: Decoder[Currency] = Decoder.decodeString.map(Currency(_))
  implicit val show: Show[Currency]       = Show.show[Currency](c => s"[${c.symbol}]")
}

case class SourceCurrency(currency: Currency, issuer: Option[AccountAddr])

object SourceCurrency {
  implicit val encoder: Encoder[SourceCurrency] = deriveEncoder[SourceCurrency]
  implicit val decoder: Decoder[SourceCurrency] = deriveDecoder[SourceCurrency]
  implicit val show: Show[SourceCurrency]       = Show.show[SourceCurrency](c => s"[${c.currency.show} {c.issuer.show}]")

}

/**
  *
  * @param currency  Standard currency code, CANNOT BE XRP though.
  * @param issuer The account that issues the currency
  */
case class Script(currency: Currency, issuer: AccountAddr)

object Script {
  implicit val show: Show[Script] = Show.show[Script](v => v.currency.show + s" Issuer: ${v.issuer.show}")

  implicit val encode: Encoder[Script] = deriveEncoder[Script]

  implicit val decode: Decoder[Script] = deriveDecoder[Script]
}
