package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support._

/**
  * Get a list of outstanding offers created by an account.
  */
case class AccountOffersRq(
    account: AccountAddr,
    limit: Limit = Limit.default,
    marker: Option[Marker] = None,
    ledger: Ledger = LedgerName.VALIDATED_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleScrollingRq

/**
  * We always provide a ledger_index in the request so don't worry about it in response (documented as optional and not
  * there if provided)
  *
  */
case class AccountOffersRs(
    account: AccountAddr,
    offers: List[Offer],
    marker: Option[Marker],
    resultLedger: Option[ResultLedger]
) extends RippleScrollingRs

object AccountOffersRq {

  val command: (String, Json) = "command" -> Json.fromString("account_offers")
  implicit val encoder: Encoder.AsObject[AccountOffersRq] = {
    deriveEncoder[AccountOffersRq]
      .mapJsonObject(o => command +: o)
      .mapJsonObject(o => Ledger.renameLedgerField(o))
  }

}

object AccountOffersRs {
  implicit val decoder: Decoder[AccountOffersRs] = deriveDecoder[AccountOffersRs]
    .product(Decoder[ResultLedger])
    .map {
      case (a, theResultLedger) =>
        a.copy(resultLedger = Some(theResultLedger))
    }
}
