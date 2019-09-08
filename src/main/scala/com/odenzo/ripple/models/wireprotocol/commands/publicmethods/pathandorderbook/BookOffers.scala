package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.pathandorderbook

import io.circe.Codec

import com.odenzo.ripple.models.atoms.ledgertree.statenodes.OfferNode
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}

/**
  * Gets a list of offers, aka order book, between two currencies.
  * Note the currency is either { "currency" : "XRP"} or { "currency" :"USD", "issuer":"..." } form
  * https://ripple.com/build/rippled-apis/#book-offers
  * So, thehack will be Option with Issuer, None = XRP.
  */
case class BookOffersRq(
    taker: Option[AccountAddr] = None,
    taker_gets: Script,
    taker_pays: Script
) extends RippleRq

case class BookOffersRs(offers: List[OfferNode]) extends RippleRs

object BookOffersRq extends CirceCodecUtils {
  private type ME = BookOffersRq
  private val command: String = "book_offers"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)

}

object BookOffersRs {
  implicit val config: Configuration               = Configuration.default
  implicit val codec: Codec.AsObject[BookOffersRs] = deriveConfiguredCodec[BookOffersRs]
}
