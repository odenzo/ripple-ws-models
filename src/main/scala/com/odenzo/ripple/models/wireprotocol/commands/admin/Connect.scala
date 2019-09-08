package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto.deriveDecoder

import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo.AccountChannelsRq.wrapCommandCodec

/**
  * The documentation on rippled site is incorrect. This is correct.
  * https://ripple.com/build/rippled-apis/#connect
  *
  * You can throw in any address and it returns connecting, so this is just asking it to attempt it.
  * Not confirming it can do it.
  *
  * @param ip "ip": "192.170.145.88". Haven't tried IPV6
  */
case class ConnectRq(ip: String, port: Int = 6561) extends RippleAdminRq

/**
  * There in just a blank JSON object here
  */
case class ConnectRs(message: String) extends RippleAdminRs

object ConnectRq {
  private type ME = ConnectRq
  private val command: String = "connect"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object ConnectRs {
  implicit val decoder: Decoder[ConnectRs] = deriveDecoder[ConnectRs]
}
