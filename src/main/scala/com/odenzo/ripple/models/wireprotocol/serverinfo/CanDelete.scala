package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

/**
  * https://ripple.com/build/rippled-apis/#can-delete
  * This just takes ledgerindex now.
  * For "never" use LedgerIndex.MIN  (LedgerIndex(0))
  * For "always" use LedgerIndex.MAX  LedgerIndex(4294967295)
  * This will give an error if online_delete is not enabled on the server (which it isn't for my testnets :-()
  *
  * @param can_delete
  * @param id
  */
case class CanDeleteRq(can_delete: LedgerSequence = LedgerSequence.MIN, id: RippleMsgId = RippleMsgId.random)
    extends RippleAdminRq

case class CanDeleteRs(can_delete: LedgerSequence) extends RippleAdminRs

object CanDeleteRq {
  val command: (String, Json) = "command" -> "can_delete".asJson
  implicit val encoder: Encoder.AsObject[CanDeleteRq] = {
    deriveEncoder[CanDeleteRq].mapJsonObject(o => command +: o)
  }
}

object CanDeleteRs {
  implicit val decoder: Decoder[CanDeleteRs] = deriveDecoder[CanDeleteRs]
}
