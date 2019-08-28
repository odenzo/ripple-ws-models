package com.odenzo.ripple.models.atoms.ledgertree.transactions

import cats._
import cats.data._
import cats.implicits._
import io.circe.{JsonObject, Encoder, HCursor, Decoder, Json}
import io.circe.syntax._
import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxPayment.logger
import com.odenzo.ripple.models.atoms.{Drops, AccountAddr, RippleTime, DestinationTag}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{EscrowCancelTx, PaymentTx}

case class TxEscrowCancel(
    tx: EscrowCancelTx,
    common: TxCommon
) extends LedgerTransaction

object TxEscrowCancel {
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
