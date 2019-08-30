package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.{Json, Encoder, JsonObject, Decoder}
import io.circe.syntax._

import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction
import scala.reflect.ClassTag

import com.odenzo.ripple.models.atoms.Meta

/** Keeping around to mutate into actionable RippleEq and RippleTransactionRequest */
case class GenericLedgerTransaction[T <: RippleTransaction](tx: T, common: TxCommon)

object GenericLedgerTransaction {
  implicit def encoder[T <: RippleTransaction: Encoder: ClassTag]: Encoder[GenericLedgerTransaction[T]] =
    new Encoder[GenericLedgerTransaction[T]] { pt =>
      override def apply(a: GenericLedgerTransaction[T]): Json = {
        val rtx = a.tx: RippleTransaction
        Json.fromJsonObject(
          JsonObject.fromIterable(rtx.asJsonObject.toVector ++ a.common.asJsonObject.toVector)
        )
      }
    }

  implicit def decoder[T <: RippleTransaction: Decoder: ClassTag]: Decoder[GenericLedgerTransaction[T]] = {
    Decoder[T].product(Decoder[TxCommon]).map { case (v: T, c: TxCommon) => GenericLedgerTransaction(v, c) }
  }

}
