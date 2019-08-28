package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.semiauto.{deriveCodec, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleRs}

/**
  * Use this to advance and validate the current ledger only on a stand-alone server.
  * [[https://ripple.com/build/rippled-apis/#ledger-accept]]
  *
  * @todo Testing requires a manual rippled server
  * @param id
  */
case class LedgerAcceptRq(id: RippleMsgId = RippleMsgId.random) extends RippleAdminRq

case class LedgerAcceptRs(ledger_current_index: LedgerSequence) extends RippleRs

object LedgerAcceptRq {
  val command: (String, Json) = "command" -> "ledger_accept".asJson
  implicit val encoder: Encoder.AsObject[LedgerAcceptRq] = {
    deriveEncoder[LedgerAcceptRq].mapJsonObject(o => command +: o)
  }
}

object LedgerAcceptRs {
  implicit val decoder: Decoder[LedgerAcceptRs] = deriveCodec[LedgerAcceptRs]
}
