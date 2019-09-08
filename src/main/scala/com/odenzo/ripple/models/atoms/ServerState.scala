package com.odenzo.ripple.models.atoms

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

/** Partial decoding of the ServerState as returned in ServerInfo command */
case class ServerState(
    buildVersion: String,
    completeLedgers: LedgerIndexRange,
    ioLatencyMs: Long,
    loadBase: Long,
    loadFactor: Long,
    loadFactorFeeEscalation: Option[Long],
    loadFactorFeeReference: Option[Long],
    loadFactorServer: Option[Long],
    serverState: String
)

object ServerState {
  implicit val config: Configuration              = Configuration.default.withSnakeCaseMemberNames
  implicit val codec: Codec.AsObject[ServerState] = deriveConfiguredCodec[ServerState]
}
