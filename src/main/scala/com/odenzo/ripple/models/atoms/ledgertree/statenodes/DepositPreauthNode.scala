package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import cats.Show
import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms.{LedgerSequence, AccountAddr, RippleHash, TxnHash}
import com.odenzo.ripple.models.utils.CirceCodecUtils

// This has some standard additional fields too
// https://xrpl.org/check.html
case class DepositPreauthNode(
    account: AccountAddr,
    authorize: AccountAddr,
    flags: Long,
    ownerNode: Option[String],
    prevTxnID: Option[TxnHash],            // The 64 character hex index (key)  , proper name? LedgerNodeIndex?  Cannot
    prevTxnLgrSeq: Option[LedgerSequence], // LedgerSequence type...yeah should be called prevTxnLgrIndex then?
    index: Option[RippleHash]              // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object DepositPreauthNode {
  implicit val config: Configuration                     = CirceCodecUtils.configCapitalizeExcept()
  implicit val codec: Codec.AsObject[DepositPreauthNode] = deriveConfiguredCodec[DepositPreauthNode]
}
