package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.pathandorderbook

import com.odenzo.ripple.models.atoms._
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}

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
    source_currencies: Option[List[Currency]]
) extends RippleRq

case class RipplePathFindRs(
    alternatives: List[AlternativePaths],
    destination_account: AccountAddr,
    destination_currencies: List[Currency],
    full_reply: Option[Boolean]
) extends RippleRs

object RipplePathFindRq extends CirceCodecUtils {
  private type ME = RipplePathFindRq
  private val command: String = "ripple_path_find"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)

}

object RipplePathFindRs {
  implicit val config: Configuration                   = Configuration.default
  implicit val codec: Codec.AsObject[RipplePathFindRs] = deriveConfiguredCodec[RipplePathFindRs]
}
