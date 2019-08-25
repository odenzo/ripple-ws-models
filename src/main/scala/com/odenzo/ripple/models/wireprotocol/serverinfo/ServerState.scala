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

/** Lots of stuff, maybe we can break down internal atoms build_version, complete_ledgers amd validation_ledger most
  * common */
case class ServerStateRs(state: JsonObject) extends RippleRs

object ServerStateRq {
  implicit val encoder: Encoder.AsObject[ServerStateRq] = {
    deriveEncoder[ServerStateRq].mapJsonObject(_.add("command", "server_state".asJson))
  }
}

object ServerStateRs {
  implicit val decoder: Decoder[ServerStateRs] = deriveDecoder[ServerStateRs]
}
