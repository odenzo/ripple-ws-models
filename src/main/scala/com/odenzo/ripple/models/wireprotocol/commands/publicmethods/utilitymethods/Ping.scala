package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.utilitymethods

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo.AccountChannelsRq.wrapCommandCodec

/**
  * The documentation on rippled site is incorrect. This is correct.
  * https://ripple.com/build/rippled-apis/#ping
  *
  *

  */
case class PingRq() extends RippleRq

/**
  * There in just a blank JSON object here

  */
case class PingRs() extends RippleRs

object PingRq {
  private type ME = PingRq
  private val command: String = "ping"

  implicit val config: Configuration     = Configuration.default
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object PingRs {
  implicit val config: Configuration           = Configuration.default
  implicit val decoder: Codec.AsObject[PingRs] = deriveConfiguredCodec[PingRs]
}
