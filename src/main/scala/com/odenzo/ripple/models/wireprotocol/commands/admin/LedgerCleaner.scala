package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto.deriveCodec
import io.circe.{Codec, Decoder}

import com.odenzo.ripple.models.atoms.LedgerSequence
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}

/**
  * https://ripple.com/build/rippled-apis/#ledger-cleaner
  * Requests defaults to all nones.
  * Result is just message.
  */
case class LedgerCleanerRq(
    ledger: Option[LedgerSequence] = None,
    max_ledger: Option[LedgerSequence] = None,
    min_ledger: Option[LedgerSequence] = None,
    full: Option[Boolean] = None,
    fix_txns: Option[Boolean] = None,
    check_nodes: Option[Boolean] = None,
    stop: Option[Boolean] = None
) extends RippleAdminRq

case class LedgerCleanerRs(message: String) extends RippleAdminRs

object LedgerCleanerRq extends CirceCodecUtils {
  private type ME = LedgerCleanerRq
  private val command: String = "ledger_cleaner"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object LedgerCleanerRs {
  implicit val decoder: Decoder[LedgerCleanerRs] = deriveCodec[LedgerCleanerRs]
}
