package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import cats.Show
import io.circe.generic.extras.Configuration
import io.circe._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.atoms.{AccountAddr, UInt64, RippleHash}
import com.odenzo.ripple.models.utils.CirceCodecUtils

trait DirectoryNode extends LedgerNode

/**
  * Directory Node, this doesn't show up in delta records
  *
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
case class OfferDirectoryNode(
    rootIndex: RippleHash,
    indexes: List[LedgerNodeIndex],
    indexNext: Option[UInt64],
    indexPrev: Option[UInt64],
    owner: Option[AccountAddr],
    takerPaysCurrency: Option[String],
    takerPaysIssuer: Option[String],
    taketGetsCurrency: Option[String],
    takerGetsIssuer: Option[String]
) extends DirectoryNode

object OfferDirectoryNode {
  implicit val config: Configuration                       = CirceCodecUtils.configCapitalize
  implicit val decoder: Codec.AsObject[OfferDirectoryNode] = deriveConfiguredCodec[OfferDirectoryNode]
  implicit val show: Show[OfferDirectoryNode]              = Show.fromToString[OfferDirectoryNode]
}

/**
  * Directory Node, this doesn't show up in delta records
  *
  * @param rootIndex
  * @param indexes
  * @param indexNext
  * @param indexPrev
  * @param owner
  */
case class OwnerDirectoryNode(
    flags: Long,
    indexes: List[LedgerNodeIndex],
    owner: AccountAddr,
    rootIndex: LedgerNodeIndex,
    ledgerEntryType: String,
    indexNext: Option[UInt64],
    indexPrev: Option[UInt64]
) extends DirectoryNode

object OwnerDirectoryNode {
  implicit val config: Configuration                       = CirceCodecUtils.configCapitalize
  implicit val decoder: Codec.AsObject[OwnerDirectoryNode] = deriveConfiguredCodec[OwnerDirectoryNode]
  implicit val show: Show[OwnerDirectoryNode]              = Show.fromToString[OwnerDirectoryNode]
}
