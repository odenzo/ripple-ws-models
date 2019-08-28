package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder, deriveCodec}
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}

import com.odenzo.ripple.models.atoms.{LedgerSequence, RippleMsgId}
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

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
    stop: Option[Boolean] = None,
    id: RippleMsgId = RippleMsgId.random
) extends RippleAdminRq

case class LedgerCleanerRs(message: String) extends RippleAdminRs

object LedgerCleanerRq {
  val command: (String, Json) = "command" -> "ledger_cleaner".asJson
  implicit val encoder: Encoder.AsObject[LedgerCleanerRq] =
    deriveEncoder[LedgerCleanerRq].mapJsonObject(o => command +: o)

}

object LedgerCleanerRs {
  implicit val decoder: Decoder[LedgerCleanerRs] = deriveCodec[LedgerCleanerRs]
}
