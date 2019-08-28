package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
case class PaymentTx(
    account: AccountAddr,
    amount: CurrencyAmount,
    destination: AccountAddr,
    destinationTag: Option[DestinationTag] = None,
    invoiceID: Option[InvoiceHash] = None,
    paths: Option[PaymentPath] = None,
    sendMax: Option[CurrencyAmount] = None,
    deliverMin: Option[CurrencyAmount] = None,
    flags: BitMask[PaymentFlag] = BitMask.empty[PaymentFlag]
) extends RippleTransaction

object PaymentTx {
  private val tx: (String, Json)     = "TransactionType" -> Json.fromString("Payment")
  implicit val config: Configuration = CirceCodecUtils.capitalizeConfiguration.withDefaults

  implicit val encoder: Encoder.AsObject[PaymentTx] = deriveConfiguredEncoder[PaymentTx].mapJsonObject(tx +: _)
  implicit val decoder: Decoder[PaymentTx]          = deriveConfiguredDecoder[PaymentTx]

}
