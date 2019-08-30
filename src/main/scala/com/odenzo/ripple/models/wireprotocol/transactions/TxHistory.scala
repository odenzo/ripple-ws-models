package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.ledgertree.transactions.LedgerTxn
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRs, RippleRq}

/**
  * Decprecated command to get the last few transactions, where start is the number to skip over
  */
case class TxHistoryRq(start: Int, id: RippleMsgId = RippleMsgId.EMPTY) extends RippleRq

case class TxHistoryRs(
    index: Int,
    txs: List[LedgerTxn]
) extends RippleRs

object TxHistoryRq {
  val command: Json = "tx_history".asJson
  implicit val encoder: Encoder.AsObject[TxHistoryRq] = {
    deriveEncoder[TxHistoryRq].mapJsonObject(_.add("command", command))
  }
}

object TxHistoryRs {

  import io.circe._

  implicit val decoder: Decoder[TxHistoryRs] = deriveDecoder[TxHistoryRs]

}
