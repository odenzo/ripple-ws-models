package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxCommon
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction

case class TxRq(transaction: TxnHash, binary: Boolean = false, id: RippleMsgId = RippleMsgId.EMPTY) extends RippleRq

/**
  * This is for the tx transaction inquiry, compare with TxNode and refactor.
  * We reuse TxNode but if it isn't validated then None because missing some mandatory items.
  * THIS should probably be fixed up to deal with TxRs when transaction is not in validated ledger!
  * Either[TxNodePending,TxNode] ?
  */
case class TxRs(
    tx: RippleTransaction,
    common: TxCommon
) extends RippleRs

object TxRq {
  val command: (String, Json) = "command" -> "tx".asJson
  implicit val encoder: Encoder.AsObject[TxRq] = {
    deriveEncoder[TxRq].mapJsonObject(o => command +: o)
  }
}

object TxRs {

  import io.circe._
  import io.circe.syntax._

  implicit val decoder: Decoder[TxRs] = Decoder[RippleTransaction].product(Decoder[TxCommon]).map(v => TxRs(v._1, v._2))

  implicit val encoder: Encoder[TxRs] = new Encoder[TxRs] { pt =>
    override def apply(a: TxRs): Json = {
      Json.fromJsonObject(JsonObject.fromIterable(a.tx.asJsonObject.toVector ++ a.common.asJsonObject.toVector))
    }
  }

}
