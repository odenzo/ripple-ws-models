package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.serverinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.commands.admin.LedgerRequestRq.wrapCommandCodec

/**
  * https://ripple.com/build/rippled-apis/#server-info
  * TODO: Do at least partial decoding. Good to see if partial can be done leaving the left-overs only in Json
  * @param id
  */
case class ServerInfoRq(id: RippleMsgId = RippleMsgId.random) extends RippleRq
case class ServerInfoRs(info: Json) extends RippleRs

object ServerInfoRq {
  private type ME = ServerInfoRq
  private val command: String = "server_info"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object ServerInfoRs {
  implicit val config: Configuration               = Configuration.default
  implicit val codec: Codec.AsObject[ServerInfoRs] = deriveConfiguredCodec
}
