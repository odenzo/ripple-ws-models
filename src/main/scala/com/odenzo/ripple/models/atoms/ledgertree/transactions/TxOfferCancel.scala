package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.{JsonObject, Encoder, HCursor, Decoder, Json}
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxPayment.logger
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{OfferCancelTx, PaymentTx}

case class TxOfferCancel(
    tx: OfferCancelTx,
    common: TxCommon
)

object TxOfferCancel {
  implicit val encoder: Encoder[TxOfferCancel] = new Encoder[TxOfferCancel] { pt =>
    override def apply(a: TxOfferCancel): Json = {
      Json.fromJsonObject(JsonObject.fromIterable(a.tx.asJsonObject.toVector ++ a.common.asJsonObject.toVector))
    }
  }

  implicit val decoder: Decoder[TxOfferCancel] = (c: HCursor) => {
    for {
      tx     <- c.as[OfferCancelTx]
      common <- c.as[TxCommon]
    } yield TxOfferCancel(tx, common)
  }
}
