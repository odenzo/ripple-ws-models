package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods

import io.circe.generic.extras.Configuration
import io.circe._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.transactions.LedgerTxn
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}

/**
  * Decprecated command to get the last few transactions, where start is the number to skip over
  */
case class TxHistoryRq(start: Int) extends RippleRq

case class TxHistoryRs(index: Int, txs: List[LedgerTxn]) extends RippleRs

object TxHistoryRq extends CirceCodecUtils {
  private type ME = TxHistoryRq
  private val command: String            = "tx_history"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object TxHistoryRs {
  import io.circe._
  implicit val config: Configuration              = Configuration.default
  implicit val codec: Codec.AsObject[TxHistoryRs] = deriveConfiguredCodec[TxHistoryRs]

}
