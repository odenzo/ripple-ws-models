package com.odenzo.ripple.models.atoms.ledgertree

import io.circe.{Codec, Encoder, Decoder}
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.transactions.LedgerTransactions

/** You can look up ledger headers user ledger inquiry. Testing for this does that.
  * If current ledger then no ledger_hash is returned but ledger_index used not ledger_current_index
  * Again, `current` ledger information are a bane, and for no actual use-case (for me).
  * The close_ will not be there. This one might be worth make LedgerHeader and LedgerHeaderClosed
  * But do it this way, and decide between a case class mapping.
  * Either LedgerHeader.or[LedgerClosedHeader] appraoch or LedgerHeader (w/ options) then LedgerHeader =>
  * LedgerCloseInfo
  *
  * More like LedgerRoot or LedgetContents
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
    transactions: Option[LedgerTransactions]     // optfield, array or array of hash or expanded json
)

object LedgerHeader {

  val baseDecoder = deriveDecoder[LedgerHeader] // closed_info always None

  implicit val encoder: Encoder.AsObject[LedgerHeader] = deriveEncoder[LedgerHeader]

  implicit val decoder: Decoder[LedgerHeader] = {
    deriveDecoder[LedgerHeader].product(Decoder[Option[LedgerClosedInfo]]).map {
      case (header, info) => header.copy(closed_info = info)
    }
  }
  // implicit val encoder: Encoder.AsObject[LedgerHeader] = deriveEncoder[LedgerHeader]
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
  implicit val codec: Codec.AsObject[LedgerClosedInfo] = deriveCodec[LedgerClosedInfo]
}
