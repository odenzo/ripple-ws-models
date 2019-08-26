package com.odenzo.ripple.models.atoms.ledgertree.nodes

import cats.Show
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.atoms.{AccountAddr, UInt64, RippleHash}
import com.odenzo.ripple.models.utils.CirceCodecUtils

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
  implicit val config: Configuration                  = CirceCodecUtils.capitalizeConfiguration
  implicit val decoder: Codec.AsObject[DirectoryNode] = deriveConfiguredCodec[DirectoryNode]
  implicit val show: Show[DirectoryNode]              = Show.fromToString[DirectoryNode]
}
