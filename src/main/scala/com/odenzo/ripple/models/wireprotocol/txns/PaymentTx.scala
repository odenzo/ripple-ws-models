package com.odenzo.ripple.models.wireprotocol.txns

import com.odenzo.ripple.models.atoms._
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}
case class PaymentTx(
    account: AccountAddr,
    amount: CurrencyAmount,
    destination: AccountAddr,
    destinationTag: Option[DestinationTag] = None,
    invoiceID: Option[InvoiceHash] = None,
    paths: Option[List[PaymentPath]] = None, // Contains an array of arrays
    sendMax: Option[CurrencyAmount] = None,
    deliverMin: Option[CurrencyAmount] = None,
    flags: BitMask[PaymentFlag] = BitMask.empty[PaymentFlag]
) extends RippleTx

object PaymentTx {
  implicit val config: Configuration            = configCapitalize.withDefaults
  private val base: Codec.AsObject[PaymentTx]   = deriveConfiguredCodec[PaymentTx]
  implicit val codec: Codec.AsObject[PaymentTx] = wrapTxnCodec(base, RippleTxnType.Payment)
}
