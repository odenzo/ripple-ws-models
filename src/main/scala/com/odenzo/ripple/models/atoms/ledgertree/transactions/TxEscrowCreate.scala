package com.odenzo.ripple.models.atoms.ledgertree.transactions
import cats._
import cats.data._
import cats.implicits._
import io.circe.{JsonObject, Encoder, HCursor, Decoder, Json}
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxPayment.logger
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{EscrowCreateTx, PaymentTx}

/** Flags ommitted since only GLobal Flag */
case class TxEscrowCreate(
    tx: EscrowCreateTx,
    common: TxCommon
) extends LedgerTransaction

object TxEscrowCreate {

  implicit val encoder: Encoder[TxEscrowCreate] = new Encoder[TxEscrowCreate] { pt =>
    override def apply(a: TxEscrowCreate): Json = {
      Json.fromJsonObject(JsonObject.fromIterable(a.tx.asJsonObject.toVector ++ a.common.asJsonObject.toVector))
    }
  }

  implicit val decoder: Decoder[TxEscrowCreate] = (c: HCursor) => {
    for {
      tx     <- c.as[EscrowCreateTx]
      common <- c.as[TxCommon]
    } yield TxEscrowCreate(tx, common)
  }
}
