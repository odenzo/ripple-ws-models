package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto.deriveDecoder

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}
import com.odenzo.ripple.models.wireprotocol.commands.admin.LedgerRequestRq.wrapCommandCodec

/**
  * https://ripple.com/build/rippled-apis/#can-delete
  * This just takes ledgerindex now.
  * For "never" use LedgerIndex.MIN  (LedgerIndex(0))
  * For "always" use LedgerIndex.MAX  LedgerIndex(4294967295)
  * This will give an error if online_delete is not enabled on the server (which it isn't for my testnets :-()
  *
  * @param can_delete

  */
case class CanDeleteRq(can_delete: LedgerSequence = LedgerSequence.MIN) extends RippleAdminRq

case class CanDeleteRs(can_delete: LedgerSequence) extends RippleAdminRs

object CanDeleteRq {
  private type ME = CanDeleteRq
  private val command: String = "can_delete"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)

}

object CanDeleteRs {
  implicit val decoder: Decoder[CanDeleteRs] = deriveDecoder[CanDeleteRs]
}
