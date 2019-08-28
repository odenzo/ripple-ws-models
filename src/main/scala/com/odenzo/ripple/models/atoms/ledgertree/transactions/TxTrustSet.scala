package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.{Decoder, HCursor}
import cats._
import cats.data._
import cats.implicits._
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxPayment.logger
import com.odenzo.ripple.models.atoms.{
  RippleTxnType,
  TxnSequence,
  RipplePublicKey,
  Drops,
  TrustSetFlag,
  FiatAmount,
  TxnHash,
  RippleTime,
  LedgerSequence,
  BitMask,
  Memos,
  AccountAddr
}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{TrustSetTx, PaymentTx}

case class TxTrustSet(
    tx: TrustSetTx,
    common: TxCommon
) extends LedgerTransaction

object TxTrustSet {
  implicit val encoder: Encoder[TxTrustSet] = new Encoder[TxTrustSet] { pt =>
    override def apply(a: TxTrustSet): Json = {
      Json.fromJsonObject(JsonObject.fromIterable(a.tx.asJsonObject.toVector ++ a.common.asJsonObject.toVector))
    }
  }

  implicit val decoder: Decoder[TxTrustSet] = (c: HCursor) => {
    for {
      tx     <- c.as[TrustSetTx]
      common <- c.as[TxCommon]
    } yield TxTrustSet(tx, common)
  }
}
