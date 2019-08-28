package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.Decoder

import com.odenzo.ripple.models.atoms._

/**
  * See also docs for account root node. I am guessing this has delta too?
  *
  */
case class SignerListNode(
    signerQuoruim: Option[AccountAddr],
    signerEntries: Option[TxnSequence],
    signerListID: Option[CurrencyAmount],
    flags: Option[Long],
    ownerNode: Option[UInt64], // LedgerNodeIndex type.
    previousTxnId: Option[TxnHash],
    previousTxnLgrSeq: Option[LedgerSequence],
    index: Option[String] // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object SignerListNode {

  implicit val decode: Decoder[SignerListNode] =
    Decoder.forProduct8(
      "SignerQuorum",
      "SignerEntries",
      "SignerListID",
      "Flags",
      "OwnerNode",
      "PreviousTxnID",
      "PreviousTxnLgrSeq",
      "index"
    )(SignerListNode.apply)

}
