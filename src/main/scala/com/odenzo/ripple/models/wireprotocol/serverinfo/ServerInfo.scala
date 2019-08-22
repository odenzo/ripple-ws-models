package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  * https://ripple.com/build/rippled-apis/#server-info
  * TODO: Do at least partial decoding. Good to see if partial can be done leaving the left-overs only in Json
  * @param id
  */
case class ServerInfoRq(id: RippleMsgId = RippleMsgId.random) extends RippleRq
case class ServerInfoRs(info: Json)                           extends RippleRs

object ServerInfoRq {
  val command: (String, Json) = "command" -> "server_info".asJson
  implicit val encoder: Encoder.AsObject[ServerInfoRq] = {
    deriveEncoder[ServerInfoRq].mapJsonObject(o => command +: o)
  }
}

object ServerInfoRs {
  implicit val decoder: Decoder[ServerInfoRs] = deriveDecoder[ServerInfoRs]
}
