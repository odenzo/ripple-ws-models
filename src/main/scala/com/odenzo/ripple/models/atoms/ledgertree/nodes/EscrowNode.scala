package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe.Decoder

import com.odenzo.ripple.models.atoms._

/**
  * See also docs for account root node. I am guessing this has delta too?
  * @param account
  * @param flags
  * @param sequence
  * @param balance
  * @param ownerCount
  * @param prevTxnId
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
    prevTxnId: Option[TxnHash],            // The 64 character hex index (key)  , proper name? LedgerNodeIndex?  Cannot
    prevTxnLgrSeq: Option[LedgerSequence], // LedgerSequence type...yeah should be called prevTxnLgrIndex then?
    index: Option[String]                  // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object EscrowNode {

  implicit val decode: Decoder[EscrowNode] =
    Decoder.forProduct13(
      "Account",
      "Desintation",
      "Amount",
      "Condition",
      "CancelAfter",
      "FinishAfter",
      "SourceTag",
      "DestinationTag",
      "Flags",
      "OwnerNode",
      "PreviousTxnID",
      "PreviousTxnLgrSeq",
      "index"
    )(EscrowNode.apply)

}
