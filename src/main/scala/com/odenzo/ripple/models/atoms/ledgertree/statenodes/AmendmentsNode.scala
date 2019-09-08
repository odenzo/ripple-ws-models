package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * https://ripple.com/build/transactions/#enableamendment
  * https://ripple.com/build/amendments/
  * Amendments
  */
case class AmendmentsNode(flags: Long, amendments: List[LedgerNodeIndex], index: LedgerNodeIndex) extends LedgerNode

object AmendmentsNode {
  implicit val config: Configuration                 = CirceCodecUtils.configCapitalizeExcept()
  implicit val codec: Codec.AsObject[AmendmentsNode] = deriveConfiguredCodec[AmendmentsNode]

}
// TODO: Encode the amendment flags. May as well bit flag it, even though only two values now.
