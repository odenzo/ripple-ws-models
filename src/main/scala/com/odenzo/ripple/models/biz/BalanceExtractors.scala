package com.odenzo.ripple.models.biz

import cats.implicits._
import io.circe.JsonObject
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeDelta
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.caterrors.OError
import com.odenzo.ripple.models.wireprotocol.accountinfo.AccountTxRs
import com.odenzo.ripple.models.wireprotocol.transactions.TxRs
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.PaymentTx

/**
  * Some utility functions for extracting the current balance of accounts in a payment transaction.
  * Extracts from AccountTx and Tx meta entries - the ledger nodes.
  * This is limited to Payment Transactions for now --- and simple Xrp -> Xrp and Fiat -> Fiat (not forex).
  * Also haven't looked how Rippling will effect it.
  *
  * This is used to get running balances per transaction, normally you would sequence the transaction by time and/or
  * sequence number of the transaction in the account.
  *
  * A transaction has a send and receiver. Balances change on both.
  * This extracts all changes, and you filter by account (and currency).
  * Note that other transactions also effect the XRP balance in terms of transaction fees being deducted.
  * But, the use case for this is running balances for transactions, just doing XRP because it is interesting
  * for some strange use cases (however the real interest for XRP is the available abalance san reserve fees)
  *
  */
trait BalanceExtractors {

  /**
    * Each account can have multiple balances, should be just one per currency i think.
    * extract by filtering for now, since current use case is to get for a particular account and currency.
    * Where did the previous balance extractor go?
    * @param rs
    * FIXME: Note, for fiat cases we can now just get transactions by type.
    * @return
    */
  def extractBalance(rs: TxRs): List[AccountBalance] = {
    if (!rs.validated) OError("Transaction Not Validated").asLeft
    val parties: Option[(AccountAddr, AccountAddr)] = rs.tx match {
      case tx: PaymentTx => Some(Tuple2(tx.account, tx.destination))
      case other         => None
    }

    val delivered: CurrencyAmount = rs.meta.flatMap(m => m.delivered_amount).getOrElse(Drops(0))

    val allBalances: List[AccountBalance] = rs.meta.map(extractBalancesFromMeta).getOrElse(List.empty[AccountBalance])
    allBalances
  }

  /** Top level response with have a list of Transaction records, we get old balances for each tx/tx record */
  def extractBalance(rs: AccountTxRs): List[List[AccountBalance]] = {
    val sorted = rs.transactions //.sortBy(_.tx.sequence.v)

    val trimmed = sorted //.slice(0, 1)
    // Note: Not all of these transactions will be Payment Transactions.
    val validated = rs.transactions.filter(tx => tx.validated)
    val extracted = trimmed.map { txn =>
      extractBalance(txn)
    }
    extracted
  }

  /**
    *
    * @param transaction The transaction node from AccountTxRs which may or may not be Payment Node etc. Assumes
    *                    validated though
    */
  def extractBalance(transaction: TransactionRecord): List[AccountBalance] = extractBalancesFromMeta(transaction.meta)

  /**
    * AccountRoot tends to have balance changes for XRP, perhaps not always though.
    *
    *
    * @return Possibly empty list of all balances found
    *
    */
  // Assumption is always in final fields? newFields and previousFields maybe interesting too.
  // Note: Working for XRP transaction at least.
  // Note this will process Modified/CreatedNode just the same for now.

  private def extractBalancesFromMeta(meta: Meta): List[AccountBalance] = {

    val trimmed = meta.affectedNodes.zipWithIndex
    val balances: List[AccountBalance] = trimmed
      .flatMap {
        case (anode, indx) =>
          val balances: Option[List[AccountBalance]] = anode.modifiedNode.map { v =>
            extractBalances(v)
          }

          val initBalance: Option[List[AccountBalance]] = anode.createdNode.map { v =>
            extractBalances(v)
          }

          val combined: List[AccountBalance] = (balances |+| initBalance).getOrElse(List.empty[AccountBalance])

          combined
      }

    balances

  }

  /**
    * Parse RippleStateEntry for any balance movements and get the latest balance.
    * This will typically have Fiat Balance *and* XRP balance movement for the sending account.
    * For the Fiat stuff, the relative trust-line (positive and negative amounts) are a little tricky.
    * Only issuers accounts should have negative balances on a trustline in our scenarios because
    * we have central banks and users of the money. More generally trustlines can be bi-directional though.
    *
    * So, care is taken to make sure the high and low ordering of account addresses is handled.
    * Much testing needed.
    *
    * @param entry
    *
    */
  def extractBalances(entry: LedgerNodeDelta): List[AccountBalance] = {
    val balances = entry.ledgerEntryType match {
      case "RippleState" => extractFiatFromRippleState(entry)
      case "AccountRoot" => extractXrpBalance(entry)
      case other =>
        List.empty[AccountBalance]

    }
    balances
  }

  /** This has to be a RippleState node for FiatBalance, note there may be some XRP balance change too?
    * If it is differnt node then just inefficiently ignores. */
  private def extractXrpBalance(entry: LedgerNodeDelta): List[AccountBalance] = {
    // A little tricker due to high and low on the limits. This is really a transaction on limit lines.
    // Need to be aware of the account_one and the differences when the transfer is directly from the issuer.
    val debug                          = entry.finalFields.map(_.asJson.spaces2).getOrElse("No Final Fields")
    val debugCreated                   = entry.newFields.map(_.asJson.spaces2).getOrElse("No Created Fields")
    val fieldList: List[JsonObject]    = List(entry.newFields, entry.finalFields).flatten
    val balances: List[AccountBalance] = fieldList.flatMap(decodeXrp)
    balances
  }

  /** Extract XRP from a delta ledger node, failing with empty list if not present (or errors!) */
  protected def decodeXrp(obj: JsonObject): List[AccountBalance] = {
    val bal: Option[Drops]          = obj("Balance").flatMap(_.as[Drops].toOption)
    val addr: Option[AccountAddr]   = obj("Account").flatMap(_.as[AccountAddr].toOption)
    val res: Option[AccountBalance] = (addr, bal).mapN(AccountBalance(_, _))
    res.toList
  }

  /** This has to be a RippleState node for FiatBalance, note there may be some XRP balance change too?
    * If it is differnt node then just inefficiently ignores. */
  private def extractFiatFromRippleState(entry: LedgerNodeDelta): List[AccountBalance] = {
// A little tricker due to high and low on the limits. This is really a transaction on limit lines.
// Need to be aware of the account_one and the differences when the transfer is directly from the issuer.
    val trustlines: Option[(FiatAmount, FiatAmount, FiatAmount)] = for {
      finalFields <- entry.finalFields
      balance     <- finalFields("Balance").flatMap(_.as[FiatAmount].toOption)
      highLimit   <- finalFields("HighLimit").flatMap(_.as[FiatAmount].toOption)
      lowLimit    <- finalFields("LowLimit").flatMap(_.as[FiatAmount].toOption)

    } yield (balance, lowLimit, highLimit)

// Now we need to walk through the cases.

// First: If the balance has issuer ONE_ACCOUNT then we know its from Issuer to third party
    val maybeBalances: Option[List[AccountBalance]] = trustlines.map {
      case (bal, low, high) =>
        val negBalance: Boolean = bal.amount < 0
        val amount              = bal.amount
        val lowAccount          = low.script.issuer
        val highAccount         = high.script.issuer

// FIXME: Placeholder for this logic pending a third party transfer investigation
        if (negBalance) {
          AccountBalance(highAccount, FiatAmount(amount.abs, low.script)) ::
            AccountBalance(lowAccount, FiatAmount(amount, low.script)) ::
            Nil
        } else {
          AccountBalance(highAccount, FiatAmount(amount, low.script).negate) ::
            AccountBalance(lowAccount, FiatAmount(amount, low.script)) ::
            Nil
        }
    }
    maybeBalances.getOrElse(List.empty[AccountBalance])
  }
}

object BalanceExtractors extends BalanceExtractors
