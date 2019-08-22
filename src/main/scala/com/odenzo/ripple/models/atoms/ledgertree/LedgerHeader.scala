package com.odenzo.ripple.models.atoms.ledgertree

import io.circe.Decoder
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._

trait LedgerHeader

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
case class LedgerClosedHeader(
    ledger_index: LedgerSequence, // Noted as String Number or Number! FMe
    ledger_hash: Option[LedgerHash],
    account_hash: Option[AccountHash],
    close_time: RippleTime,
    closed: Boolean,
    parent_hash: LedgerHash,
    total_coins: Drops,
    transaction_hash: TxnHash,
    close_time_resolution: Int,
    close_flags: Int, // Actually their docs outdated.
    accepted: Option[Boolean],
    parent_close_time: RippleTime, // Not sure why this is missing on closed if we have a hash.
    // I thought there is only even one open ledger. Once this ledger current (or closed) then its parent must have been
    // closed. Maybe a race condition in code. TODO: Take a peek at ledger transitioning
    accountState: Option[List[LedgerNodeIndex]], // optional field and may be empty array
    transactions: Option[List[LedgerNodeIndex]]  // optional field and may be empty array
) extends LedgerHeader

case class LedgerCurrentHeader(
    ledger_index: LedgerSequence, // Noted as String Number or Number! FMe
    ledger_hash: Option[LedgerHash],
    account_hash: Option[AccountHash],
    close_time: Option[RippleTime],
    closed: Boolean,
    parent_hash: LedgerHash,
    total_coins: Drops,
    transaction_hash: TxnHash,
    close_time_resolution: Option[Int],
    close_flags: Option[Int], // Actually their docs outdated.
    accepted: Option[Boolean],
    parent_close_time: Option[RippleTime], // Not sure why this is missing on closed if we have a hash.
    // I thought there is only even one open ledger. Once this ledger open (or closed) then its parent must have been
    // closed. Maybe a race condition in code. TODO: Take a peek at ledger transitioning
    accountState: Option[List[LedgerNodeIndex]], // optional field and may be empty array
    transactions: Option[List[LedgerNodeIndex]]  // optional field and may be empty array
) extends LedgerHeader

object LedgerHeader {

  implicit val decodeClosed: Decoder[LedgerClosedHeader]   = deriveDecoder[LedgerClosedHeader]
  implicit val decodeCurrent: Decoder[LedgerCurrentHeader] = deriveDecoder[LedgerCurrentHeader]
  implicit val decoder: Decoder[LedgerHeader] = {
    val closed: Decoder[LedgerHeader]  = decodeClosed.map(v => v: LedgerHeader)
    val current: Decoder[LedgerHeader] = decodeCurrent.map(v => v: LedgerHeader)
    closed.or(current)
  }

}
