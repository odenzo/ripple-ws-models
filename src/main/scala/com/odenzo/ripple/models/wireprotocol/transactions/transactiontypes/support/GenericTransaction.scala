package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support

import io.circe.{Json, Encoder, Decoder}
import io.circe.syntax._
import scala.reflect.ClassTag

import com.odenzo.ripple.models.atoms.ledgertree.transactions.{GenericLedgerTransaction, TxCommon}

/** I am not sure how well this will work on second attempt  */
case class GenericTransaction[T <: RippleTransaction](tx: T)

object GenericTransaction {
  implicit def encoder[T <: RippleTransaction: Encoder.AsObject: ClassTag]: Encoder[GenericTransaction[T]] =
    new Encoder[GenericTransaction[T]] { pt =>
      override def apply(a: GenericTransaction[T]): Json = {
        val rtx: RippleTransaction = a.tx: RippleTransaction
        rtx.asJson
      }
    }

  implicit def decoder[T <: RippleTransaction: Decoder: ClassTag]: Decoder[GenericLedgerTransaction[T]] = {
    Decoder[T].product(Decoder[TxCommon]).map { case (v: T, c: TxCommon) => GenericLedgerTransaction(v, c) }
  }

}
