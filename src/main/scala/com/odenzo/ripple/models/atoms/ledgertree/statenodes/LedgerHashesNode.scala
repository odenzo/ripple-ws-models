package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import cats.Show
import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms.LedgerSequence
import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.utils.CirceCodecUtils

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
  implicit val config: Configuration                   = CirceCodecUtils.configCapitalizeExcept()
  implicit val codec: Codec.AsObject[LedgerHashesNode] = deriveConfiguredCodec[LedgerHashesNode]
}
