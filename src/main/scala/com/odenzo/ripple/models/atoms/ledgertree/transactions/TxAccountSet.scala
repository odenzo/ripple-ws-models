package com.odenzo.ripple.models.atoms.ledgertree.transactions
import io.circe.syntax._

import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.AccountSetTx
import io.circe._

/** AN entry like this doesn't actually have to have any flags. */
case class TxAccountSet(
    tx: AccountSetTx,
    common: TxCommon
) extends LedgerTransaction

object TxAccountSet {
  implicit val encoder: Encoder[TxAccountSet] = new Encoder[TxAccountSet] { pt =>
    override def apply(a: TxAccountSet): Json = {
      Json.fromJsonObject(JsonObject.fromIterable(a.tx.asJsonObject.toVector ++ a.common.asJsonObject.toVector))
    }
  }

  implicit val decoder: Decoder[TxAccountSet] = (c: HCursor) => {
    for {
      tx     <- c.as[AccountSetTx]
      common <- c.as[TxCommon]
    } yield TxAccountSet(tx, common)
  }
}
