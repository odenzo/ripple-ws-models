package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.serverinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms.ServerState
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.commands.admin.LedgerRequestRq.wrapCommandCodec

/**
  * https://ripple.com/build/rippled-apis/#server-state
  *  TODO: Medium priority decoding of ServerState Response
  */
case class ServerStateRq() extends RippleRq

/** Lots of stuff, maybe we can break down internal atoms build_version, complete_ledgers amd validation_ledger most
  * common */
case class ServerStateRs(state: ServerState) extends RippleRs

object ServerStateRq {
  private type ME = ServerStateRq
  private val command: String = "server_state"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object ServerStateRs {
  implicit val config: Configuration                = Configuration.default
  implicit val codec: Codec.AsObject[ServerStateRs] = deriveConfiguredCodec
}
