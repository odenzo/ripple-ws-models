package com.odenzo.ripple.models.atoms.ledgertree

import cats._
import cats.data._
import cats.implicits._

import io.circe.{Decoder, Json}

import com.odenzo.ripple.models.atoms._

/**
  * The `account_info` inquiry returns this variant of account root ledger under account_data
  * Should be merged with AccountRootEntry , this has (additionally?) signer_lists and
  * probably the queue_data (optional in AccountInfo call)
  * https://ripple.com/build/ledger-format/#accountroot
  */
case class AccountData(
    account: AccountAddr,
    balance: Drops,
    flags: BitMask[AccountRootFlag], // Bitmap flag, can be zero
    ledgerEntryType: String,         // Should always be AccountRoot
    ownerCount: Int,
    prevTxn: Option[TxnHash], // Not there for CreatedNode
    prevLegrSeq: Option[LedgerSequence],
    sequence: TxnSequence,
    index: TxnHash,                 // 64 char.
    signer_list: Option[List[Json]] // Field can not be there, or be empty array
)

// TODO: Convert to deriveDecoder with name mapping (upcase and align abbreviations)
object AccountData {
  implicit val decode: Decoder[AccountData] =
    Decoder.forProduct10(
      "Account",
      "Balance",
      "Flags",
      "LedgerEntryType",
      "OwnerCount",
      "PreviousTxnID",
      "PreviousTxnLgrSeq",
      "Sequence",
      "index",
      "signer_list"
    )(AccountData.apply)

  implicit val show: Show[AccountData] = Show.fromToString[AccountData]
}
