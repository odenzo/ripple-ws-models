package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.ledgerinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto.deriveCodec

import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo.AccountChannelsRq.wrapCommandCodec

/** Gets the id of the last closed ledger.
  * [[https://ripple.com/build/rippled-apis/#ledger-closed]]
  **/
case class LedgerClosedRq() extends RippleRq

/** The response falls under the CommonCmdRs */
case class LedgerClosedRs() extends RippleRs

object LedgerClosedRq {
  private type ME = LedgerClosedRq
  private val command: String = "ledger_closed"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object LedgerClosedRs {
  implicit val codec: Codec.AsObject[LedgerClosedRs] = deriveCodec[LedgerClosedRs]
}
