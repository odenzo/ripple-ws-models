package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/** Gets the id of the last closed ledger.
  * [[https://ripple.com/build/rippled-apis/#ledger-closed]]
  **/
case class LedgerClosedRq(id: RippleMsgId = RippleMsgId.random) extends RippleRq

case class LedgerClosedRs(ledger_hash: LedgerHash, ledger_index: LedgerSequence) extends RippleRs

object LedgerClosedRq {
  val command: (String, Json) = "command" -> "ledger_closed".asJson
  implicit val encoder: Encoder.AsObject[LedgerClosedRq] = {
    deriveEncoder[LedgerClosedRq].mapJsonObject(o => command +: o)
  }
}

object LedgerClosedRs {
  implicit val decoder: Decoder[LedgerClosedRs] = deriveDecoder[LedgerClosedRs]
}
