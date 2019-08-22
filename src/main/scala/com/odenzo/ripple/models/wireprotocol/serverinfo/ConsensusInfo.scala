package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

/**
  * https://ripple.com/build/rippled-apis/#consensus-info
  *
  * No decoding of result. Docs note subject to change, so just extract as needed.
  *
  *
  * @param id
  */
case class ConsensusInfoRq(id: RippleMsgId = RippleMsgId.random) extends RippleAdminRq
case class ConsensusInfoRs(info: Json)                           extends RippleAdminRs

object ConsensusInfoRq {
  val command: (String, Json) = "command" -> "consensus_info".asJson
  implicit val encoder: Encoder.AsObject[ConsensusInfoRq] = {
    deriveEncoder[ConsensusInfoRq].mapJsonObject(o => command +: o)
  }
}

object ConsensusInfoRs {
  implicit val decoder: Decoder[ConsensusInfoRs] = deriveDecoder[ConsensusInfoRs]
}
