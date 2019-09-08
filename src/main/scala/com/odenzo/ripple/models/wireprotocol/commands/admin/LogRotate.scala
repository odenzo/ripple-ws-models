package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto.deriveDecoder

import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleAdminRq}

/**
  * https://ripple.com/build/rippled-apis/#logrotate
  *
  */
case class LogRotateRq() extends RippleAdminRq

case class LogRotateRs(message: String) extends RippleRs

object LogRotateRq extends CirceCodecUtils {
  private type ME = LogRotateRq
  private val command: String = "log_rotate"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object LogRotateRs {
  implicit val decoder: Decoder[LogRotateRs] = deriveDecoder[LogRotateRs]
}
