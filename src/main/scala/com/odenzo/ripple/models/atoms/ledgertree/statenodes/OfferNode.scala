package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * See also docs for account root node. I am guessing this has delta too?
  * [[https://ripple.com/build/rippled-apis/#book-offers]] is using this now too.
  * [[https://ripple.com/build/transactions/#offercreate]]
  * FIXME: OfferNode but see also TxNode TxOfferCreate. Referred to as OfferCreateNode in docs. :-(
  */
case class OfferNode(
    account: AccountAddr,
    flags: Option[Long],
    bookDirectory: Option[LedgerIndex],
    bookNode: Option[String], // really an option ?

    sequence: Option[TxnSequence],
    takerPays: Option[CurrencyAmount],
    takerGets: Option[CurrencyAmount],
    expiration: Option[RippleTime],
    ownerNode: Option[String], // LedgerNodeIndex type? "0000000000000000" So, its a LedgerId?
    previousTxnId: Option[TxnHash],
    previousTxnLgrSeq: Option[LedgerSequence],
    index: Option[RippleHash],                 // Guessing this is a LedgerNodeIndex of this node.
    owner_funds: Option[String],               // Truly is option
    quality: Option[BigDecimal],               // Truly is option
    taker_gets_funded: Option[CurrencyAmount], // Truly is option
    taker_pays_funded: Option[CurrencyAmount]  // Truly is option
) extends LedgerNode

object OfferNode {
  implicit val config: Configuration            = CirceCodecUtils.configCapitalizeExcept(Set("owner_funds"))
  implicit val codec: Codec.AsObject[OfferNode] = deriveConfiguredCodec[OfferNode]
}
