package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.{Meta, _}
import com.odenzo.ripple.models.support.{RippleRq, RippleRs, TxnStatusCode}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{
  ValidatedTxData,
  PendingTxData,
  RippleTransaction
}

case class TxRq(transaction: TxnHash, binary: Boolean = false, id: RippleMsgId = RippleMsgId.EMPTY) extends RippleRq

/**
  * This is for the tx transaction inquiry, compare with TxNode and refactor.
  * We reuse TxNode but if it isn't validated then None because missing some mandatory items.
  * THIS should probably be fixed up to deal with TxRs when transaction is not in validated ledger!
  * Either[TxNodePending,TxNode] ?

  * @param tx    The transaction node, This should always be present on non-error cases?
  * @param meta  Present when? Only when validated I think.
  * @param validated None or false is not in validated ledger (e.g. failed or pending). Only true is true.
  */
case class TxRs(
    tx: RippleTransaction,
    txnData: PendingTxData,
    inLedger: Option[ValidatedTxData],
    meta: Option[Meta],
    validated: Boolean
) extends RippleRs {
  def txstatus: Option[TxnStatusCode] = meta.map(_.transactionResult)

}

object TxRq {
  val command: (String, Json) = "command" -> "tx".asJson
  implicit val encoder: Encoder.AsObject[TxRq] = {
    deriveEncoder[TxRq].mapJsonObject(o => command +: o)
  }
}

object TxRs {

  import io.circe._

  implicit val decoder: Decoder[TxRs] = Decoder.instance[TxRs] { cursor =>
    val isValidated = cursor.get[Boolean]("validated").getOrElse(false)

    for {
      meta     <- cursor.get[Option[Meta]]("meta") // Not there is failed, or not validated
      txn      <- cursor.as[RippleTransaction] // Extract the Transaction based on TransactionType
      pending  <- cursor.as[PendingTxData] // Should be there, except for sever error case?
      accepted <- cursor.as[Option[ValidatedTxData]]
      // Skipping either Pending or Commited Extra data for now.
    } yield TxRs(txn, pending, accepted, meta, isValidated)
  }

}
