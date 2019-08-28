package com.odenzo.ripple.models.atoms.ledgertree.transactions

import cats._
import cats.data._
import cats.implicits._
import io.circe.{JsonObject, Encoder, HCursor, Decoder, Json}
import io.circe.syntax._
import com.odenzo.ripple.models.atoms.{Drops, AccountAddr, RippleTime, DestinationTag}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{EscrowFinishTx, EscrowCreateTx}

/** Flags ommitted since only GLobal Flag */
case class TxEscrowFinish(
    tx: EscrowFinishTx,
    common: TxCommon
) extends LedgerTransaction

object TxEscrowFinish {

  implicit val encoder: Encoder[TxEscrowFinish] = new Encoder[TxEscrowFinish] { pt =>
    override def apply(a: TxEscrowFinish): Json = {
      Json.fromJsonObject(JsonObject.fromIterable(a.tx.asJsonObject.toVector ++ a.common.asJsonObject.toVector))
    }
  }

  implicit val decoder: Decoder[TxEscrowFinish] = (c: HCursor) => {
    for {
      tx     <- c.as[EscrowFinishTx]
      common <- c.as[TxCommon]
    } yield TxEscrowFinish(tx, common)
  }
}
