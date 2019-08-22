package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe.Decoder

import com.odenzo.ripple.models.atoms._

/**
  * See also docs for account root node. I am guessing this has delta too?
  * [[https://ripple.com/build/rippled-apis/#book-offers]] is using this now too.
  * [[https://ripple.com/build/transactions/#offercreate]]
  * FIXME: OfferNode but see also TxNode TxOfferCreate. Referred to as OfferCreateNode in docs. :-(
  */
case class OfferNode(
    flags: Option[Long],
    account: Option[AccountAddr],
    sequence: Option[TxnSequence],
    takerPays: Option[CurrencyAmount],
    takerGets: Option[CurrencyAmount],
    bookDirectory: Option[LedgerIndex],
    bookNode: Option[String], // really an option
    expiration: Option[RippleTime],
    ownerNode: Option[String], // LedgerNodeIndex type? "0000000000000000" So, its a LedgerId?
    previousTxnId: Option[TxnHash],
    previousTxnLgrSeq: Option[LedgerSequence],
    index: Option[String],       // Guessing this is a LedgerNodeIndex of this node.
    owner_funds: Option[String], // Not documented but present. Value field for GETS? Replaces
    // taker_get/pays_funded
    quality: BigDecimal,
    taker_gets_funded: Option[CurrencyAmount],
    taker_pays_funded: Option[CurrencyAmount]
) extends LedgerNode

object OfferNode {

  implicit val decode: Decoder[OfferNode] =
    Decoder.forProduct16(
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
      "index",
      "owner_funds",
      "quality",
      "taker_gets_funded",
      "taker_pays_funded"
    )(OfferNode.apply)

}
