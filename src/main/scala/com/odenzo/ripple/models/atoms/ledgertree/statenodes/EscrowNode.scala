package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe._
import io.circe.generic.extras.semiauto._
import io.circe.generic.extras.Configuration

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

case class EscrowNode(
    account: AccountAddr,
    amount: Option[Drops],
    condition: Option[String], // SHA256?
    cancelAfter: Option[RippleTime],
    destination: Option[AccountAddr], // Think not optional
    destinationNode: Option[String],
    finishAfter: Option[RippleTime],
    sourceTag: Option[DestinationTag],
    destinationTag: Option[DestinationTag],
    flags: Option[Long],
    prevTxnID: Option[TxnHash],            // The 64 character hex index (key)  , proper name? LedgerNodeIndex?  Cannot
    prevTxnLgrSeq: Option[LedgerSequence], // LedgerSequence type...yeah should be called prevTxnLgrIndex then?
    index: Option[RippleHash]              // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object EscrowNode {
  implicit val config: Configuration             = CirceCodecUtils.configCapitalizeExcept()
  implicit val codec: Codec.AsObject[EscrowNode] = deriveConfiguredCodec[EscrowNode]
}
