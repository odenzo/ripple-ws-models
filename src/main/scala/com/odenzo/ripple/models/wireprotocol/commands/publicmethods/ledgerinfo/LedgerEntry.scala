package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.ledgerinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.statenodes.AccountRootNode
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleScrollingRq, RippleScrollingRs}

/** This is a generic ledger entry browser. I think I will break it down, or just leave
  * people to deal with dynamic results as Jsons */
case class LedgerEntryRq(
    tipe: String                      = "account_root",
    account_root: Option[AccountAddr] = None,
    index: Option[String]             = None, // Not a LedgerIndex, a LedgerNodeIndex
    binary: Boolean                   = false
) extends RippleScrollingRq

case class LedgerEntryRs(
    index: String, // Not reallt?
    node: Option[AccountRootNode],
    node_binary: Option[String]
) extends RippleScrollingRs

// LedgerAccept and LedgerRequest skipped. I don't use most of these.

object LedgerEntryRq extends CirceCodecUtils {
  private type ME = LedgerEntryRq
  private val command: String            = "ledger_entry"
  implicit val config: Configuration     = configWithTipe.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object LedgerEntryRs {
  implicit val config: Configuration                = Configuration.default
  implicit val codec: Codec.AsObject[LedgerEntryRs] = deriveConfiguredCodec[LedgerEntryRs]

}
