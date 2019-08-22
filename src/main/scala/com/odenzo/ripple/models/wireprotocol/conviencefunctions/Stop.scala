package com.odenzo.ripple.models.wireprotocol.conviencefunctions

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

/**
  * Again this seems to break the standard based on documentation. Haven't tested.
  * https://ripple.com/build/rippled-apis/#fetch-info
  * TODO: Low Priority decoding the json rs
  * @param id
  */
case class StopRq(id: RippleMsgId = RippleMsgId.random) extends RippleAdminRq
case class StopRs(message: String)                      extends RippleAdminRs

object StopRq {
  val command: (String, Json) = "command" -> "stop".asJson
  implicit val encoder: Encoder.AsObject[StopRq] = {
    deriveEncoder[StopRq].mapJsonObject(o => command +: o)
  }
}

object StopRs {
  implicit val decoder: Decoder[StopRs] = deriveDecoder[StopRs]
}
