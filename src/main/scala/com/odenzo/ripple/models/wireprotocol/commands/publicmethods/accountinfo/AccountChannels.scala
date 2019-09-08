package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._
import monocle.macros.Lenses

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleScrollingRq, RippleScrollingRs}

/**
  * https://ripple.com/build/rippled-apis/#account-channels
  *
  * @param account
  * @param destination_account
  */
@Lenses("_") case class AccountChannelsRq(account: AccountAddr, destination_account: Option[AccountAddr])
    extends RippleScrollingRq

case class AccountChannelsRs(account: AccountAddr, channels: List[RippleChannel]) extends RippleScrollingRs

object AccountChannelsRq extends CirceCodecUtils {

  private type ME = AccountChannelsRq
  private val command: String = "account_channels"

  val defaultResponse: AccountChannelsRs.type = AccountChannelsRs

  implicit val config: Configuration     = Configuration.default
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)

}

object AccountChannelsRs {
  implicit val config: Configuration                    = Configuration.default
  implicit val codec: Codec.AsObject[AccountChannelsRs] = deriveConfiguredCodec[AccountChannelsRs]
}
