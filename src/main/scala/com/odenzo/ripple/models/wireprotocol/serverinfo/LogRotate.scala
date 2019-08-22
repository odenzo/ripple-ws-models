package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleRs}

/**
  * https://ripple.com/build/rippled-apis/#logrotate
  *
  * @param id
  */
case class LogRotateRq(id: RippleMsgId = RippleMsgId.random) extends RippleAdminRq

case class LogRotateRs(message: String) extends RippleRs

object LogRotateRq {
  val command: (String, Json) = "command" -> "logrotate".asJson
  implicit val encoder: Encoder.AsObject[LogRotateRq] = {
    deriveEncoder[LogRotateRq].mapJsonObject(o => command +: o)
  }
}

object LogRotateRs {
  implicit val decoder: Decoder[LogRotateRs] = deriveDecoder[LogRotateRs]
}
