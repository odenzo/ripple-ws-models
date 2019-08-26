package com.odenzo.ripple.models.support

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.biz.{BalanceExtractors, AccountBalance}
import com.odenzo.ripple.models.testkit.CodecTesting

/**
  * Balance extractors are all tested in BasicTransactionsTest under the API module.
  */
class BalanceExtractorsTest extends CodecTesting {

  def balanceExtract(rec: TransactionRecord): List[AccountBalance] = {
    val balances = BalanceExtractors.extractBalance(rec)
    logger.debug(s"Balances: $balances")
    balances
  }
  def isXrpPaymentSent(rec: TransactionRecord): Boolean = {
    logger.debug(s"Tx Type ${rec.tx.txType}")
    if (rec.tx.txType != RippleTxnType.Payment) false
    else {
      val txp = rec.tx.asInstanceOf[TxPayment]
      txp.amount match {
        case Drops(_)         => true
        case FiatAmount(_, _) => false
      }
    }
  }
}
