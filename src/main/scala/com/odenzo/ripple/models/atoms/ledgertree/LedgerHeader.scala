package com.odenzo.ripple.models.atoms.ledgertree

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.RippleTransaction

/** You can look up ledger headers user ledger inquiry. Testing for this does that.
  * If current ledger then no ledger_hash is returned but ledger_index used not ledger_current_index
  * Again, `current` ledger information are a bane, and for no actual use-case (for me).
  * The close_ will not be there. This one might be worth make LedgerHeader and LedgerHeaderClosed
  * But do it this way, and decide between a case class mapping.
  * Either LedgerHeader.or[LedgerClosedHeader] appraoch or LedgerHeader (w/ options) then LedgerHeader =>
  * LedgerCloseInfo
  *
  * Trtying LedgerClose and LedgerCurrent appraoch, where Closed can be Accepted or Validated. Anything but current.
  * [[https://ripple.com/build/ledger-format/#tree-format]]
  * [[https://ripple.com/build/ledger-format/#header-format]]
  * */
case class LedgerHeader(
    ledger_index: LedgerSequence, // Noted as String Number or Number! FMe
    ledger_hash: Option[LedgerHash],
    account_hash: Option[AccountHash],
    parent_hash: LedgerHash,
    total_coins: Drops,
    transaction_hash: TxnHash,
    parent_close_time: RippleTime,
    closed_info: Option[LedgerClosedInfo],
    accountState: Option[List[LedgerNodeIndex]], // optional field and may be empty array
    transactions: Option[LedgerTransactions]     // optional field and may be empty array or array of hash or
    // expanded json
)

object LedgerHeader {

  val baseDecoder = deriveDecoder[LedgerHeader] // closed_info always None
  implicit val decoder: Decoder[LedgerHeader] = {
    deriveDecoder[LedgerHeader].product(Decoder[Option[LedgerClosedInfo]]).map {
      case (header, info) => header.copy(closed_info = info)
    }
  }
}

/** The header when information for a ledger that has not yet closed (or validated) */
case class LedgerClosedInfo(
    accepted: Boolean,
    closed: Boolean,
    close_time: RippleTime,
    close_time_resolution: Int,
    close_flags: Int // Actually their docs outdated becasue??
)

object LedgerClosedInfo {
  implicit val decoder: Decoder[LedgerClosedInfo] = deriveDecoder[LedgerClosedInfo]
}

/** Represent an expanded transaction is a ledger (typically validated)
  * The txn is at the top level and needs to be lifted, metaData is a top field. */
case class LedgerTxn(txn: RippleTransaction, metaData: Meta)

object LedgerTxn {
  implicit val decoder: Decoder[LedgerTxn] = Decoder.instance { hcursor =>
    for {
      txn <- hcursor.as[RippleTransaction]
      md  <- hcursor.get[Meta]("metaData")
    } yield LedgerTxn(txn, md)
  }
  //implicit val encoder: Encoder.AsObject[LedgerTxn] = deriveEncoder[LedgerTxn]
}

/** Represents a list of Ledger Tranasction that is either expanded or a */
case class LedgerTransactions(either: Either[List[LedgerTxn], List[LedgerNodeIndex]])

object LedgerTransactions {

  // Our cursor is on the Array of transactions. Each transaction is a LedgerNodeIndex or LedgerTxn
  implicit val decoder: Decoder[LedgerTransactions] = Decoder[List[LedgerTxn]]
    .either(Decoder[List[LedgerNodeIndex]])
    .map(LedgerTransactions.apply)

}
