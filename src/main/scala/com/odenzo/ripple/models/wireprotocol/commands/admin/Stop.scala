package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo.AccountChannelsRq.wrapCommandCodec

/**
  * Again this seems to break the standard based on documentation. Haven't tested.
  * https://ripple.com/build/rippled-apis/#fetch-info
  * TODO: Low Priority decoding the json rs

  */
case class StopRq()                extends RippleAdminRq
case class StopRs(message: String) extends RippleAdminRs

object StopRq {
  private type ME = StopRq
  private val command: String = "stop"

  implicit val config: Configuration     = Configuration.default
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object StopRs {
  implicit val config: Configuration         = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[StopRs] = deriveConfiguredCodec[StopRs]
}
