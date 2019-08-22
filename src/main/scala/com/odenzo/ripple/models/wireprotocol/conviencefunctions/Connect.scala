package com.odenzo.ripple.models.wireprotocol.conviencefunctions

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  * The documentation on rippled site is incorrect. This is correct.
  * https://ripple.com/build/rippled-apis/#connect
  *
  * You can throw in any address and it returns connecting, so this is just asking it to attempt it.
  * Not confirming it can do it.
  *
  * @param id
  *           @param ip "ip": "192.170.145.88". Haven't tried IPV6
  */
case class ConnectRq(ip: String, port: Int = 6561, id: RippleMsgId = RippleMsgId.random) extends RippleRq

/**
  * There in just a blank JSON object here
  */
case class ConnectRs(message: String) extends RippleRs

object ConnectRq {
  val command: (String, Json) = "command" -> "connect".asJson
  implicit val encoder: Encoder.AsObject[ConnectRq] = {
    deriveEncoder[ConnectRq].mapJsonObject(o => command +: o)
  }
}

object ConnectRs {
  implicit val decoder: Decoder[ConnectRs] = deriveDecoder[ConnectRs]
}
