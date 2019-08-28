package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder, deriveCodec}
import io.circe.syntax._
import com.odenzo.ripple.models.utils.CirceCodecUtils.withCommand
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/** Gets the id of the last closed ledger.
  * [[https://ripple.com/build/rippled-apis/#ledger-closed]]
  **/
case class LedgerClosedRq(id: RippleMsgId = RippleMsgId.random) extends RippleRq

case class LedgerClosedRs(ledger_hash: LedgerHash, ledger_index: LedgerSequence) extends RippleRs

object LedgerClosedRq {
  implicit val encoder: Encoder.AsObject[LedgerClosedRq] = {
    deriveEncoder[LedgerClosedRq].mapJsonObject(withCommand("ledger_closed"))
  }
}

object LedgerClosedRs {
  implicit val codec: Codec.AsObject[LedgerClosedRs] = deriveCodec[LedgerClosedRs]
}
