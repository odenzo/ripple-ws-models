package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe.Decoder

import com.odenzo.ripple.models.atoms.LedgerSequence
import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex

/**
  * Documents where?  Relatively short list of node pointers, guess they are all to some other LedgerNodes.
  * Maybe they are all directory nodes, check sometime
  * @param flags
  * @param hashes
  * @param lastLedgerSequence
  * @param index
  */
case class LedgerHashesNode(
    flags: Int,
    hashes: List[LedgerNodeIndex],
    lastLedgerSequence: LedgerSequence,
    index: LedgerNodeIndex
) extends LedgerNode

object LedgerHashesNode {

  implicit val decode: Decoder[LedgerHashesNode] =
    Decoder.forProduct4(
      "Flags",
      "Hashes",
      "LastLedgerSequence",
      "index"
    )(LedgerHashesNode.apply)

}
