package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe.Decoder

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex

/**
  * https://ripple.com/build/transactions/#enableamendment
  * https://ripple.com/build/amendments/
  * Amendments
  */
case class AmendmentsNode(flags: Long, amendments: List[LedgerNodeIndex], index: LedgerNodeIndex) extends LedgerNode

object AmendmentsNode {

  implicit val decode: Decoder[AmendmentsNode] =
    Decoder.forProduct3(
      "Flags",
      "Amendments",
      "index"
    )(AmendmentsNode.apply)

}
// TODO: Encode the amendment flags. May as well bit flag it, even though only two values now.
