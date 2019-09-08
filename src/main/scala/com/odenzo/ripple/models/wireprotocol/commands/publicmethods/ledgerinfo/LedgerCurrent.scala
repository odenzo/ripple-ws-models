package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.ledgerinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo.AccountChannelsRq.wrapCommandCodec

/**
  * [[https://ripple.com/build/rippled-apis/#ledger-current]]
  * Gets the id of the current (in-progress) ledger, returns
  *  This is a test of having ledger_current_index HERE and in the CommonRs, since it is
  *   germane to this call.
  */
case class LedgerCurrentRq()                                         extends RippleRq
case class LedgerCurrentRs(ledger_current_index: LedgerCurrentIndex) extends RippleRs

object LedgerCurrentRq {
  private type ME = LedgerCurrentRq
  private val command: String = "ledger_current"

  implicit val config: Configuration     = Configuration.default
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object LedgerCurrentRs {
  implicit val config: Configuration                  = Configuration.default
  implicit val codec: Codec.AsObject[LedgerCurrentRs] = deriveConfiguredCodec

}
