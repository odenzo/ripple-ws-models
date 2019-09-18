package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * See also docs for account root node. I am guessing this has delta too?
  *
  */
case class PayChannelNode(
    account: Option[AccountAddr],
    amount: Drops,  // in quoted form
    balance: Drops, // in quoted form
    destination: AccountAddr,
    flags: Option[Long],
    ownerNode: Option[String], // LedgerNodeIndex type.
    previousTxnId: Option[TxnHash],
    previousTxnLgrSeq: Option[LedgerSequence],
    publicKey: RipplePublicKey,
    settleDelay: Option[Long],
    sourceTag: Option[SourceTag],
    index: Option[RippleHash] // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object PayChannelNode {
  implicit val config: Configuration                 = CirceCodecUtils.configCapitalizeExcept(Set("index"))
  implicit val codec: Codec.AsObject[PayChannelNode] = deriveConfiguredCodec[PayChannelNode]

}
