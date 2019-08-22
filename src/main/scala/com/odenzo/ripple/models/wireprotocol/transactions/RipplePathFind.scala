package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  *
  * https://ripple.com/build/rippled-apis/#path-find      https://ripple.com/build/rippled-apis/#ripple-path-find
  * NOTE: TODO: I will implement path_find too. Under subscriptions maybe.
  * NOTE2: -1 XRP or -1 Amount on a Fiat Amount can be used for unlimited.
  */
case class RipplePathFindRq(
    source_account: AccountAddr,
    destination_account: AccountAddr,
    destination_amount: CurrencyAmount,
    send_max: Option[CurrencyAmount],
    source_currencies: Option[List[Currency]],
    ledger: LedgerIndex = LedgerName.CURRENT_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq

case class RipplePathFindRs(
    alternatives: List[AlternativePaths],
    destination_account: AccountAddr,
    destination_currencies: List[Currency],
    full_reply: Option[Boolean]
) extends RippleRs

object RipplePathFindRq {
  private val command: (String, Json) = "command" -> Json.fromString("ripple_path_find")
  implicit val encoder: Encoder.AsObject[RipplePathFindRq] = {
    deriveEncoder[RipplePathFindRq].mapJsonObject(o => command +: o)

  }

}

object RipplePathFindRs {
  implicit val decoder: Decoder[RipplePathFindRs] = deriveDecoder[RipplePathFindRs]
}
