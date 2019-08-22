package com.odenzo.ripple.models.atoms.ledgertree.nodes

import cats.Show
import io.circe.Decoder

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.atoms.{AccountAddr, RippleHash, UInt64}

/**
  * Directory Node, this doesn't show up in delta records
  * @param rootIndex
  * @param indexes
  * @param indexNext
  * @param indexPrev
  * @param owner
  * @param takerPaysCurrency
  * @param takerPaysIssuer
  * @param taketGetsCurrency
  * @param takerGetsIssuer
  */
case class DirectoryNode(
    rootIndex: RippleHash,
    indexes: List[LedgerNodeIndex],
    indexNext: Option[UInt64],
    indexPrev: Option[UInt64],
    owner: Option[AccountAddr],
    takerPaysCurrency: Option[String],
    takerPaysIssuer: Option[String],
    taketGetsCurrency: Option[String],
    takerGetsIssuer: Option[String]
) extends LedgerNode

object DirectoryNode {

  implicit val decoder: Decoder[DirectoryNode] = Decoder.forProduct9(
    "RootIndex",
    "Indexes",
    "IndexNext",
    "IndexPrevious",
    "Owner",
    "TakerPaysCurrency",
    "TakerPaysIssuer",
    "TakerGetsCurrency",
    "TakerGetsIssuer"
  )(DirectoryNode.apply)

  implicit val show: Show[DirectoryNode] = Show.fromToString[DirectoryNode]
}
