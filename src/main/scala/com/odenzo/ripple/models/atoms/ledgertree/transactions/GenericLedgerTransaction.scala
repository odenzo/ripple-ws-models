package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.{Decoder, Encoder, Json, JsonObject}
import io.circe.syntax._
import scala.reflect.ClassTag

import com.odenzo.ripple.models.wireprotocol.txns.RippleTx

/** Keeping around to mutate into actionable RippleEq and RippleTransactionRequest */
case class GenericLedgerTransaction[T <: RippleTx](tx: T, common: LedgerTxCommon)

object GenericLedgerTransaction {
  implicit def encoder[T <: RippleTx: Encoder: ClassTag]: Encoder[GenericLedgerTransaction[T]] =
    new Encoder[GenericLedgerTransaction[T]] { pt =>
      override def apply(a: GenericLedgerTransaction[T]): Json = {
        val rtx = a.tx: RippleTx
        Json.fromJsonObject(
          JsonObject.fromIterable(rtx.asJsonObject.toVector ++ a.common.asJsonObject.toVector)
        )
      }
    }

  implicit def decoder[T <: RippleTx: Decoder: ClassTag]: Decoder[GenericLedgerTransaction[T]] = {
    Decoder[T].product(Decoder[LedgerTxCommon]).map { case (v: T, c: LedgerTxCommon) => GenericLedgerTransaction(v, c) }
  }

}
