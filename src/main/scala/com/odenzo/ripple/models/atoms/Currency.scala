package com.odenzo.ripple.models.atoms

import cats._
import cats.implicits._
import io.circe._
import io.circe.generic.semiauto._
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
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

case class SourceCurrency(currency: Currency, issuer: Option[AccountAddr])

object SourceCurrency {
  implicit val config: Configuration                 = Configuration.default
  implicit val codec: Codec.AsObject[SourceCurrency] = deriveConfiguredCodec[SourceCurrency]
  implicit val show: Show[SourceCurrency]            = Show.show[SourceCurrency](c => s"[${c.currency.show} {c.issuer.show}]")

}

/**
  *
  * @param currency  Standard currency code, CANNOT BE XRP though.
  * @param issuer The account that issues the currency
  */
case class Script(currency: Currency, issuer: AccountAddr)

object Script {
  implicit val show: Show[Script]            = Show.show[Script](v => v.currency.show + s" Issuer: ${v.issuer.show}")
  implicit val config: Configuration         = Configuration.default
  implicit val codec: Codec.AsObject[Script] = deriveConfiguredCodec[Script]
}
