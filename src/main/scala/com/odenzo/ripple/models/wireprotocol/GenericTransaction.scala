package com.odenzo.ripple.models.wireprotocol

import io.circe.{Json, Encoder, Decoder}
import io.circe.syntax._
import scala.reflect.ClassTag

import com.odenzo.ripple.models.atoms.ledgertree.transactions.{GenericLedgerTransaction, TxCommon}
import com.odenzo.ripple.models.wireprotocol.txns.RippleTx

/** I am not sure how well this will work on second attempt. Keep around for posterity  */
case class GenericTransaction[T <: RippleTx](tx: T)

object GenericTransaction {
  implicit def encoder[T <: RippleTx: Encoder.AsObject: ClassTag]: Encoder[GenericTransaction[T]] =
    new Encoder[GenericTransaction[T]] { pt =>
      override def apply(a: GenericTransaction[T]): Json = {
        val rtx: RippleTx = a.tx: RippleTx
        rtx.asJson
      }
    }

  implicit def decoder[T <: RippleTx: Decoder: ClassTag]: Decoder[GenericLedgerTransaction[T]] = {
    Decoder[T].product(Decoder[TxCommon]).map { case (v: T, c: TxCommon) => GenericLedgerTransaction(v, c) }
  }

}
