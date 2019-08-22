package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.{Meta, _}
import com.odenzo.ripple.models.support.{RippleRs, TxnStatusCode, RippleRq}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{
  PendingTxData,
  RippleTransaction,
  ValidatedTxData
}

/**
  * Decprecated command to get the last few transactions, where start is the number to skip over
  */
case class TxHistoryRq(start: Int, id: RippleMsgId = RippleMsgId.EMPTY) extends RippleRq

case class TxHistoryRs(
    index: Int,
    txs: List[RippleTransaction]
) extends RippleRs {}

object TxHistoryRq {
  val command: (String, Json) = "command" -> "tx_history".asJson
  implicit val encoder: Encoder.AsObject[TxRq] = {
    deriveEncoder[TxRq].mapJsonObject(o => command +: o)
  }
}

object TxHistoryRs {

  import io.circe._

  implicit val decoder: Decoder[TxRs] = deriveDecoder[TxRs]

}
