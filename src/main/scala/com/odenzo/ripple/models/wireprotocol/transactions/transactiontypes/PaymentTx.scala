package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

case class PaymentTx(
    account: AccountAddr,
    amount: CurrencyAmount,
    destination: AccountAddr,
    invoiceID: Option[InvoiceHash] = None,
    paths: Option[PaymentPath] = None,
    sendMax: Option[CurrencyAmount] = None,
    deliverMin: Option[CurrencyAmount] = None,
    flags: BitMask[PaymentFlag] = BitMask.empty[PaymentFlag]
) extends RippleTransaction {

  //val txnType: RippleTxnType = RippleTxnType.PaymentTxn

}

object PaymentTx {
  private val tx: (String, Json) = "TransactionType" -> Json.fromString("Payment")

  // Better to use mapJsonObject and derive encoder?
  implicit val derivedEncoder: Encoder.AsObject[PaymentTx] = {
    deriveEncoder[PaymentTx]
      .mapJsonObject(o => tx +: o)
      .mapJsonObject(o => CirceCodecUtils.upcaseFields(o))
  }
  implicit val decoder: Decoder[PaymentTx] = Decoder.instance[PaymentTx] { cursor =>
    for {
      acct       <- cursor.get[AccountAddr]("Account")
      owner      <- cursor.get[CurrencyAmount]("Amount")
      dest       <- cursor.get[AccountAddr]("Destination")
      invoice    <- cursor.get[Option[InvoiceHash]]("InvoiceID")
      paths      <- cursor.get[Option[PaymentPath]]("Paths")
      sendMax    <- cursor.get[Option[CurrencyAmount]]("SendMax")
      deliverMin <- cursor.get[Option[CurrencyAmount]]("DeliverMin")
      flags      <- cursor.get[BitMask[PaymentFlag]]("Flags")
    } yield PaymentTx(acct, owner, dest, invoice, paths, sendMax, deliverMin, flags)
  }

}
