package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}

/**
  * https://ripple.com/build/rippled-apis/#wallet-propose
  * I should use Either but not strongly typed to make clear. Waiting for Scala upgrade
  * @param key_type  Recommend to leave as default
  */
case class WalletProposeRq(
    seed: Option[String] = None,
    passphrase: Option[String] = None,
    key_type: Option[String] = None
) extends RippleAdminRq

case class WalletProposeRs(keys: AccountKeys) extends RippleAdminRs

object WalletProposeRq extends CirceCodecUtils {

  private type ME = WalletProposeRq
  private val command: String            = "wallet_propose"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object WalletProposeRs {

  import io.circe.generic.extras.semiauto._
  implicit val codec: Codec[WalletProposeRs] = deriveUnwrappedCodec[WalletProposeRs]

}
