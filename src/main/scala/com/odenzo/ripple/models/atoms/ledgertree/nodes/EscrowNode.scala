package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
import io.circe.Decoder
import io.circe.generic.extras.Configuration

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * See also docs for account root node. I am guessing this has delta too?
  * @param account
  * @param flags
  * @param sequence
  * @param balance
  * @param ownerCount
  * @param prevTxnID
  * @param prevTxnLgrSeq
  * @param accountTxnId
  * @param regularKeyAddr
  * @param emailHash
  * @param messagePubKey
  * @param tickSize
  * @param transferRate
  * @param domain
  * @param index
  */
case class EscrowNode(
    account: Option[AccountAddr],     // Think not optional
    destination: Option[AccountAddr], // Think not optional
    amount: Option[Drops],
    condition: Option[String], // SHA256?
    cancelAfter: Option[RippleTime],
    finishAfter: Option[RippleTime],
    sourceTag: Option[AccountTag],
    desintationTag: Option[AccountTag],
    flags: Option[Long],
    ownerNode: Option[UInt64],
    prevTxnID: Option[TxnHash],            // The 64 character hex index (key)  , proper name? LedgerNodeIndex?  Cannot
    prevTxnLgrSeq: Option[LedgerSequence], // LedgerSequence type...yeah should be called prevTxnLgrIndex then?
    index: Option[String]                  // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object EscrowNode {
  implicit val config: Configuration             = CirceCodecUtils.capitalizeExcept
  implicit val codec: Codec.AsObject[EscrowNode] = deriveConfiguredCodec[EscrowNode]
}
