package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.ledgerinfo

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe._

import com.odenzo.ripple.models.atoms.ledgertree.statenodes.LedgerNode
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleScrollingRq, RippleScrollingRs}

/** https://ripple.com/build/rippled-apis/#ledger-data
  *  TODO: 0.70 or thereabouts added ability to filter:
  * type allows the rpc client to specify what type of ledger
  * entries to retrieve. The available types are:
  **
  *"account"
  * "amendments"
  * "directory"
  * "fee"
  * "hashes"
  * "offer"
  * "signer_list"
  * "state"
  * "ticket"
  **/
case class LedgerDataRq(binary: Boolean = false) extends RippleScrollingRq

/**
  *
  * @param ledger Single large record of close time and other statistics including total_coins. When scrolling
  *               this field is only present on the first response. This is deprecated and may go away anyway.
  * @param state  This is for expanded state now, doesn't handle binary
  */
case class LedgerDataRs(ledger: Option[JsonObject], state: List[LedgerNode]) extends RippleScrollingRs

object LedgerDataRq extends CirceCodecUtils {
  private type ME = LedgerDataRq
  private val command: String = "ledger_data"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object LedgerDataRs {

  implicit val config: Configuration               = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[LedgerDataRs] = deriveConfiguredCodec[LedgerDataRs]

}
