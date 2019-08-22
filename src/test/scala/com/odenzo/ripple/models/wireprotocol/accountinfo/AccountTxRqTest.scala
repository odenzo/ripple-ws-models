package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe.Decoder

import com.odenzo.ripple.models.atoms.ledgertree.AffectedLedgerNode
import com.odenzo.ripple.models.atoms.{Meta, TransactionRecord}
import com.odenzo.ripple.models.testkit.CodecTesting

class AccountTxRqTest extends CodecTesting {

  val decoder: Decoder[AccountTxRs] = Decoder[AccountTxRs]

  def inspectTx(tx: TransactionRecord): Unit = {
    logger.debug(s"Ledger Index ${tx.ledger_index} Validated:  ${tx.validated}")
    logger.debug(s"Transaction ${tx.tx}")
    inspectMeta(tx.meta)
  }

  def inspectMeta(meta: Meta): Unit = {
    logger.debug(s"Meta Things ${meta.transactionIndex} ${meta.delivered_amount} ${meta.transactionResult}")
    logger.debug(s"# of AffectedThings: " + meta.affectedNodes.size)
    logger.debug(s"Affected Nodes")
    meta.affectedNodes.foreach(n => logger.debug("Affected Node: " + n))
  }

  def inspectAffectedNode(an: AffectedLedgerNode): Unit = {
    logger.info(s"\t Modified: ${an.modifiedNode}")
    logger.info(s"\t Created: ${an.createdNode}")

  }

}
