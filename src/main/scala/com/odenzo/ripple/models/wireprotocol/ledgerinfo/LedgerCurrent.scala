package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  * [[https://ripple.com/build/rippled-apis/#ledger-current]]
  * Gets the id of the current (in-progress) ledger.
  * @param id
  */
case class LedgerCurrentRq(id: RippleMsgId = RippleMsgId.random)     extends RippleRq
case class LedgerCurrentRs(ledger_current_index: LedgerCurrentIndex) extends RippleRs

object LedgerCurrentRq {
  val command: (String, Json) = "command" -> "ledger_current".asJson
  implicit val encoder: Encoder.AsObject[LedgerCurrentRq] = {
    deriveEncoder[LedgerCurrentRq].mapJsonObject(o => command +: o)
  }
}

object LedgerCurrentRs {
  implicit val decoder: Decoder[LedgerCurrentRs] = deriveDecoder[LedgerCurrentRs]

}
