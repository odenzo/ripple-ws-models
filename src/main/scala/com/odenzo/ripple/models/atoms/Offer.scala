package com.odenzo.ripple.models.atoms

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec

import com.odenzo.ripple.models.utils.CirceCodecUtils

/** TODO: As used in account_offers inquiry and Book_offers. Needs testing with both
// I think this is implemented somewhere, just lost. OfferNode similar but with comoon ledger node stuff.
// @see TxOfferCreate and OfferNode  (which should be used for now).
  */
case class Offer(
    expiration: Option[RippleTime],
    flags: BitMask[OfferCreateFlag],
    quality: BigDecimal, // Redundant
    seq: Option[TxnSequence],
    taker_gets: CurrencyAmount,
    taker_pays: CurrencyAmount
)

object Offer extends CirceCodecUtils {

  implicit val config: Configuration        = Configuration.default
  implicit val codec: Codec.AsObject[Offer] = deriveConfiguredCodec[Offer]

}
