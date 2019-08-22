package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe.Decoder

import com.odenzo.ripple.models.atoms._

/**
  * See also docs for account root node. I am guessing this has delta too?
  *
  */
case class PayChannelNode(
    flags: Option[Long],
    account: Option[AccountAddr],
    sequence: Option[TxnSequence],
    takerPays: Option[CurrencyAmount],
    takerGets: Option[CurrencyAmount],
    bookDirectory: Option[AccountAddr],
    bookNode: Option[UInt64], // really an option
    expiration: Option[RippleTime],
    ownerNode: Option[UInt64], // LedgerNodeIndex type.
    previousTxnId: Option[TxnHash],
    previousTxnLgrSeq: Option[LedgerSequence],
    index: Option[String] // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object PayChannelNode {

  implicit val decode: Decoder[PayChannelNode] =
    Decoder.forProduct12(
      "Flags",
      "Account",
      "Sequence",
      "TakerPays",
      "TakerGets",
      "BookDirectory",
      "BookNode",
      "Expiration",
      "OwnerNode",
      "PreviousTxnID",
      "PreviousTxnLgrSeq",
      "index"
    )(PayChannelNode.apply)

}
