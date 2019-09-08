package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo.AccountOffersRq.wrapCommandCodec

/**
  * Used to calculate Gateway balances by excluding certain accounts (hot wallets)
  * Good way to see what issuers have issued basically.
  * https://ripple.com/build/rippled-apis/#gateway-balances
  * TODO: Only partially implemented on response side, experiment and see what the Objects arr
  */
case class GatewayBalancesRq(
    account: AccountAddr,
    strict: Boolean             = true,
    hotwallet: Seq[AccountAddr] = Seq.empty[AccountAddr]
) extends RippleRq

case class GatewayBalancesRs(
    account: AccountAddr,
    obligations: Option[Json],
    balances: Option[Json],
    assets: Option[Json]
) extends RippleRs

object GatewayBalancesRq {

  private type ME = GatewayBalancesRq
  private val command: String            = "gateway_balances"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object GatewayBalancesRs {
  implicit val codec: Codec.AsObject[GatewayBalancesRs] = deriveCodec[GatewayBalancesRs]

}
