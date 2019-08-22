package com.odenzo.ripple.models.wireprotocol.conviencefunctions

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  * The documentation on rippled site is incorrect. This is correct.
  * https://ripple.com/build/rippled-apis/#ping
  *
  *
  * @param id
  */
case class PingRq(id: RippleMsgId = RippleMsgId.random) extends RippleRq

/**
  * There in just a blank JSON object here
  * @param role I guess will be admin or somethign else.
  */
case class PingRs(role: String) extends RippleRs

object PingRq {
  val command: (String, Json) = "command" -> "ping".asJson
  implicit val encoder: Encoder.AsObject[PingRq] = {
    deriveEncoder[PingRq].mapJsonObject(o => command +: o)
  }
}

object PingRs {
  implicit val decoder: Decoder[PingRs] = deriveDecoder[PingRs]
}
