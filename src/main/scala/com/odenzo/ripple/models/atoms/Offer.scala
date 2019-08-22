package com.odenzo.ripple.models.atoms

import io.circe.{Decoder, Encoder}

import com.odenzo.ripple.models.utils.CirceCodecUtils

/** TODO: As used in account_offers inquiry and Book_offers. Needs testing with both
// I think this is implemented somewhere, just lost. OfferNode similar but with comoon ledger node stuff.
// @see TxOfferCreate and OfferNode  (which should be used for now).
  */
case class Offer(
    expiration: Option[RippleTime],
    flags: BitMask[OfferCreateFlag],
    quality: BigDecimal, // Redundant
    sequence: Option[TxnSequence],
    taker_gets: CurrencyAmount,
    taker_pays: CurrencyAmount
)

object Offer extends CirceCodecUtils {
  implicit val encoder: Encoder.AsObject[Offer] = {
    Encoder.forProduct6("expiration", "flags", "quality", "seq", "taker_gets", "taker_pays")(
      v => (v.expiration, v.flags, v.quality, v.sequence, v.taker_gets, v.taker_pays)
    )
  }

  implicit val decoder: Decoder[Offer] = {
    Decoder.forProduct6("expiration", "flags", "quality", "seq", "taker_gets", "taker_pays")(Offer.apply)
  }

}
