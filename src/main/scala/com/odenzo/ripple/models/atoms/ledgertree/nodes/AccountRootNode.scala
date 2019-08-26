package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe.{Decoder, Codec}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.AccountData
import com.odenzo.ripple.models.utils.CirceCodecUtils

// https://ripple.com/build/ledger-format/#accountroot, probably should merge in AccountData and AccountRootEntry
// which is anemic now. Actually, looks like it is broken.
// Notes; It is starting to look like these Node entries vary more on CreatedNode, ModifiedNode that type (AccountRoot/RippleState).
// Keep both varying until elaborate them all.
// Little confused between LedgerNodeIndex and TransactionHash. 64 char hashes, sometimes hard to tell semantics though.
// Okay=== the list is slowly dawning.
// These are all possible fields in a AccountRoot LedgerNode.
// You can get the "node" with LedgerEntry but things like Tx (and AccountTx from memory) will
// break down a ModifiedNode for instance info FinalFields, FinalFields, NewFields, PreviousFields. Basically a delta
// (wow, incremental state saving in time warp, been a while since I thought about that!)
// TODO: See if can write a transformer to shapeless record or tuple to eliminate options.
// flatmap to params of case class basically.
case class AccountRootNode(
    account: Option[AccountAddr],              // Think not optional
    flags: Option[Long],                       // Bitmask[AccountRootFlag]
    sequence: Option[UInt32],                  // Account Sequence?
    balance: Option[Drops],                    // Account Balance, including reserve
    ownerCount: Option[UInt32],                // Number of objects owner has in the ledger?
    previousTxnID: Option[String],             // Account or Ledger based?
    previousTxnLgrSeq: Option[LedgerSequence], // LedgerSequence type...yeah should be called prevTxnLgrIndex then?
    accountTxnId: Option[TxnHash],             // Most recent txn submitted on account
    regularKey: Option[AccountAddr],           // Address of the regular key (if set)
    emailHash: Option[String],                 // Hash128 type
    messageKey: Option[String],                // Max 33 bytes, different than RipplePublicKey (?)
    tickSize: Option[UInt32],                  // Actually, UInt8, TODO: TickSize type?
    transferRate: Option[UInt32],              // TODO: TransferRate type (think so)
    domain: Option[String],                    // Internet domain associated with account.
    index: Option[String]                      // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object AccountRootNode {
  implicit val config: Configuration                  = CirceCodecUtils.capitalizeExcept
  implicit val codec: Codec.AsObject[AccountRootNode] = deriveConfiguredCodec[AccountRootNode]
}
