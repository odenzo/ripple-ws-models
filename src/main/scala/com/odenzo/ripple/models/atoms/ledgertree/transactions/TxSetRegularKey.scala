package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.{JsonObject, Decoder, HCursor, Encoder}
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.SetRegularKeyTx

case class TxSetRegularKey(
    tx: SetRegularKeyTx,
    common: TxCommon
) extends LedgerTransaction

object TxSetRegularKey {

  implicit val encoder: Encoder[TxSetRegularKey] = new Encoder[TxSetRegularKey] { pt =>
    override def apply(a: TxSetRegularKey): Json = {
      Json.fromJsonObject(JsonObject.fromIterable(a.tx.asJsonObject.toVector ++ a.common.asJsonObject.toVector))
    }
  }

  implicit val decoder: Decoder[TxSetRegularKey] = (c: HCursor) => {
    for {
      tx     <- c.as[SetRegularKeyTx]
      common <- c.as[TxCommon]
    } yield TxSetRegularKey(tx, common)
  }
}
