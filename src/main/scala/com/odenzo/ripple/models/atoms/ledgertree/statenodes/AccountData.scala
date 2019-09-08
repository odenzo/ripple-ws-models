package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import cats._
import cats.data._
import cats.implicits._

import com.odenzo.ripple.models.atoms._
import io.circe.{Json, _}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * The `account_info` inquiry returns this variant of account root ledger under account_data
  * Should be merged with AccountRootEntry , this has (additionally?) signer_lists and
  * probably the queue_data (optional in AccountInfo call)
  * TODO: Reconcile with AccountRootNode (which should be canonical Account Root ledger node.
  * https://ripple.com/build/ledger-format/#accountroot
  */
case class AccountData(
    account: AccountAddr,
    balance: Drops,
    flags: BitMask[AccountRootFlag], // Bitmap flag, can be zero
    ledgerEntryType: String,         // Should always be AccountRoot
    ownerCount: Int,
    previousTxnID: Option[TxnHash], // Not there for CreatedNode
    previousTxnLegrSeq: Option[LedgerSequence],
    sequence: TxnSequence,
    index: TxnHash,                 // 64 char.
    signer_list: Option[List[Json]] // Field can not be there, or be empty array
)

// TODO: Convert to deriveDecoder with name mapping (upcase and align abbreviations)
object AccountData {

  implicit val config: Configuration              = CirceCodecUtils.configCapitalizeExcept()
  implicit val codec: Codec.AsObject[AccountData] = deriveConfiguredCodec[AccountData]
  implicit val show: Show[AccountData]            = Show.fromToString[AccountData]

}
