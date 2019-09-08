package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto.deriveDecoder

import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}
import com.odenzo.ripple.models.wireprotocol.commands.admin.LedgerRequestRq.wrapCommandCodec

/**
  * https://ripple.com/build/rippled-apis/#consensus-info
  *
  * No decoding of result. Docs note subject to change, so just extract as needed.
  *
  *
  */
case class ConsensusInfoRq()           extends RippleAdminRq
case class ConsensusInfoRs(info: Json) extends RippleAdminRs

object ConsensusInfoRq {
  private type ME = ConsensusInfoRq
  private val command: String = "consensus_info"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object ConsensusInfoRs {
  implicit val decoder: Decoder[ConsensusInfoRs] = deriveDecoder[ConsensusInfoRs]
}
