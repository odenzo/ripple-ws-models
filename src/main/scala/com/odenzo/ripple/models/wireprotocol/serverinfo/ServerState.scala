package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  * https://ripple.com/build/rippled-apis/#server-state
  *  TODO: Medium priority decoding of ServerState Response
  * @param id
  */
case class ServerStateRq(id: RippleMsgId = RippleMsgId.random) extends RippleRq
case class ServerStateRs(state: Json)                          extends RippleRs

object ServerStateRq {
  val command: (String, Json) = "command" -> "server_state".asJson
  implicit val encoder: Encoder.AsObject[ServerStateRq] = {
    deriveEncoder[ServerStateRq].mapJsonObject(o => command +: o)
  }
}

object ServerStateRs {
  implicit val decoder: Decoder[ServerStateRs] = deriveDecoder[ServerStateRs]
}
