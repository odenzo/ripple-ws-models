package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.statenodes.LedgerNode
import com.odenzo.ripple.models.support.{RippleScrollingRq, RippleScrollingRs}

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
case class LedgerDataRq(
    ledger: Ledger = LedgerName.VALIDATED_LEDGER,
    limit: Limit = Limit.default,
    binary: Boolean = false,
    marker: Option[Marker] = None,
    id: RippleMsgId = RippleMsgId.random
) extends RippleScrollingRq

case class LedgerDataRs(
    ledger_hash: LedgerHash,
    ledger_index: LedgerSequence,
    marker: Option[Marker],
    state: List[LedgerNode],
    validated: Option[Boolean]
) extends RippleScrollingRs

object LedgerDataRq {
  val command: (String, Json) = "command" -> "ledger_data".asJson
  implicit val encoder: Encoder.AsObject[LedgerDataRq] = {
    deriveEncoder[LedgerDataRq]
      .mapJsonObject(o => command +: o)
      .mapJsonObject(o => Ledger.renameLedgerField(o))
  }
}

object LedgerDataRs {
  implicit val decoder: Decoder[LedgerDataRs] = deriveDecoder[LedgerDataRs]

}
