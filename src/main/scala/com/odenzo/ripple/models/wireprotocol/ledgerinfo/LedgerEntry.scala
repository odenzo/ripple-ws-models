package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.nodes.AccountRootNode
import com.odenzo.ripple.models.support.{RippleScrollingRq, RippleScrollingRs}

/**
  * This will be a complicated one and I don't use it yet.
  * https://ripple.com/build/rippled-apis/#ledger-entry
  * FIXME: THIS IS NOT OPERATIONAL YET
  * PErhaps better to have smart factory constructors in LedgerEntryRq and kitchen sink here.
  * TODO: Same with ome other helpers that are in API layer now.
  * TODO: Future roadmap implementation of LedgerEntry inquiry
  *
  */
case class LedgerEntryRq(
    mtype: String = "account_root",
    account_root: Option[AccountAddr] = None,
    ledger: Ledger = LedgerName.VALIDATED_LEDGER,
    index: Option[String] = None, // Not a LedgerIndex, a LedgerNodeIndex
    binary: Boolean = false,
    limit: Limit = Limit.default,
    marker: Option[Marker] = None,
    id: RippleMsgId = RippleMsgId.random
) extends RippleScrollingRq

case class LedgerEntryRs(
    ledger_index: LedgerSequence,
    index: String,
    node: Option[AccountRootNode],
    node_binary: Option[String],
    marker: Option[Marker]
) extends RippleScrollingRs

// LedgerAccept and LedgerRequest skipped. I don't use most of these.

object LedgerEntryRq {
  val command: (String, Json) = "command" -> "ledger_entry".asJson
  implicit val encoder: Encoder.AsObject[LedgerEntryRq] = {
    deriveEncoder[LedgerEntryRq]
      .mapJsonObject(o => command +: o)
      .mapJsonObject(o => Ledger.renameLedgerField(o))
      .mapJsonObject(o => o("mtype").fold(o)(v => o.remove("mtype").add("type", v)))

  }
}

object LedgerEntryRs {
  implicit val decoder: Decoder[LedgerEntryRs] = deriveDecoder[LedgerEntryRs]

}
