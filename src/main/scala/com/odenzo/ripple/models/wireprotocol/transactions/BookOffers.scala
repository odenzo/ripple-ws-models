package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Json, Encoder, JsonObject, Decoder}

import com.odenzo.ripple.models.atoms.ledgertree.statenodes.OfferNode
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRs, RippleRq}
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * Gets a list of offers, aka order book, between two currencies.
  * Note the currency is either { "currency" : "XRP"} or { "currency" :"USD", "issuer":"..." } form
  * https://ripple.com/build/rippled-apis/#book-offers
  * So, thehack will be Option with Issuer, None = XRP.
  */
case class BookOffersRq(
    taker: Option[AccountAddr] = None,
    taker_gets: Script,
    taker_pays: Script,
    ledger: LedgerID = LedgerName.VALIDATED_LEDGER,
    limit: Option[Long] = None,
    marker: Option[Marker] = None,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq

case class BookOffersRs(offers: List[OfferNode], ledger_index: Option[LedgerSequence], ledger_hash: Option[LedgerHash])
    extends RippleRs

object BookOffersRq extends CirceCodecUtils {

  val command: (String, Json) = "command" -> Json.fromString("book_offers")

  implicit val encoder: Encoder.AsObject[BookOffersRq] = {
    deriveEncoder[BookOffersRq].mapJsonObject(withCommandAndLedgerID("book_offers"))
  }

}

object BookOffersRs {
  implicit val decoder: Decoder[BookOffersRs] = deriveDecoder[BookOffersRs]
}
