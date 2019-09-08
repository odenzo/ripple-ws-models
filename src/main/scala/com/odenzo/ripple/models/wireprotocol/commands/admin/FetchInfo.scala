package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto.deriveDecoder

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}
import com.odenzo.ripple.models.wireprotocol.commands.admin.LedgerRequestRq.wrapCommandCodec

/**
  * https://ripple.com/build/rippled-apis/#fetch-info
  * This is another admin command used for diagnosing RCL network really.
  * @param id
  */
case class FetchInfoRq(id: RippleMsgId = RippleMsgId.random) extends RippleAdminRq

case class FetchInfoRs(info: Json) extends RippleAdminRs

object FetchInfoRq {
  private type ME = FetchInfoRq
  private val command: String = "fetch_info"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object FetchInfoRs {
  implicit val decoder: Decoder[FetchInfoRs] = deriveDecoder[FetchInfoRs]
}
