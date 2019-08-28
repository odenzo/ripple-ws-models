package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.syntax._
import io.circe._
import scribe.Logging

import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.PaymentTx

case class TxPayment(tx: PaymentTx, common: TxCommon) extends LedgerTransaction

object TxPayment extends Logging {
  implicit val encoder: Encoder[TxPayment] = new Encoder[TxPayment] { pt =>
    override def apply(a: TxPayment): Json = {
      Json.fromJsonObject(JsonObject.fromIterable(a.tx.asJsonObject.toVector ++ a.common.asJsonObject.toVector))
    }
  }

  implicit val decoder: Decoder[TxPayment] = (c: HCursor) => {
    for {
      tx     <- c.as[PaymentTx]
      common <- c.as[TxCommon]
    } yield TxPayment(tx, common)
  }
}
