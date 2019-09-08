package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto.deriveCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleAdminRq}

/**
  * Use this to advance and validate the current ledger only on a stand-alone server.
  * [[https://ripple.com/build/rippled-apis/#ledger-accept]]
  * Not sure to CommonCmdRq has to have ledger set to CURRENT or if its ignored.
  */
case class LedgerAcceptRq() extends RippleAdminRq

case class LedgerAcceptRs(ledger_current_index: LedgerSequence) extends RippleRs

object LedgerAcceptRq extends CirceCodecUtils {

  private type ME = LedgerAcceptRq
  private val command: String = "ledger_accept"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object LedgerAcceptRs {
  implicit val decoder: Decoder[LedgerAcceptRs] = deriveCodec[LedgerAcceptRs]
}
