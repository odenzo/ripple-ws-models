package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.serverinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.commands.admin.LedgerRequestRq.wrapCommandCodec

// TODO: Make Fee object and allow to levels and drops
case class FeeRq(id: RippleMsgId = RippleMsgId.random) extends RippleRq

case class FeeRs(
    current_ledger_size: Long,
    current_queue_size: Long,
    drops: FeeDrops,
    ledger_current_index: LedgerSequence,
    expected_ledger_size: Long,
    levels: FeeLevels,
    max_queue_size: Long
) extends RippleRs

object FeeRq {
  private type ME = FeeRq
  private val command: String = "fee"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object FeeRs {
  implicit val config: Configuration         = Configuration.default
  implicit val codecr: Codec.AsObject[FeeRs] = deriveConfiguredCodec[FeeRs]
}
