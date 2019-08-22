package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json, JsonObject}

import com.odenzo.ripple.models.atoms.ledgertree.nodes.OfferNode
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  * Gets a list of offers, aka order book, between two currencies.
  * Note the currency is either { "currency" : "XRP"} or { "currency" :"USD", "issuer":"..." } form
  * https://ripple.com/build/rippled-apis/#book-offers
  * So, thehack will be Option with Issuer, None = XRP.
  */
case class BookOffersRq(
    taker: Option[AccountAddr] = None,
    taker_gets: Option[Script],
    taker_pays: Option[Script],
    ledger: Ledger = LedgerName.VALIDATED_LEDGER,
    limit: Option[Long] = None,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq

case class BookOffersRs(offers: List[OfferNode], ledger_index: Option[LedgerSequence], ledger_hash: Option[LedgerHash])
    extends RippleRs

object BookOffersRq {

  val command: (String, Json) = "command" -> Json.fromString("book_offers")

  implicit val scriptEncoder: Encoder[Option[Script]] = Encoder.instance[Option[Script]] {
    case Some(script) => Encoder[Script].apply(script)
    case None         => JsonObject.singleton("currency", Json.fromString("XRP")).asJson
  }

  implicit val encoder: Encoder.AsObject[BookOffersRq] = {
    deriveEncoder[BookOffersRq]
      .mapJsonObject(o => command +: o)
      .mapJsonObject(o => Ledger.renameLedgerField(o))

  }

}

object BookOffersRs {
  implicit val decoder: Decoder[BookOffersRs] = deriveDecoder[BookOffersRs]
}
