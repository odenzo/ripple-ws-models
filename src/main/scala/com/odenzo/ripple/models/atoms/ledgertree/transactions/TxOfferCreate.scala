package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxPayment.logger
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.OfferCreateTx

/** This does seem to differ a bit from the OfferCreateNode in ledger and as returned from BookOffers */
case class TxOfferCreate(
    tx: OfferCreateTx,
    common: TxCommon
)

object TxOfferCreate {
  implicit val encoder: Encoder[TxOfferCreate] = new Encoder[TxOfferCreate] { pt =>
    override def apply(a: TxOfferCreate): Json = {
      Json.fromJsonObject(JsonObject.fromIterable(a.tx.asJsonObject.toVector ++ a.common.asJsonObject.toVector))
    }
  }

  implicit val decoder: Decoder[TxOfferCreate] = (c: HCursor) => {
    for {
      tx     <- c.as[OfferCreateTx]
      common <- c.as[TxCommon]
    } yield TxOfferCreate(tx, common)
  }
}
