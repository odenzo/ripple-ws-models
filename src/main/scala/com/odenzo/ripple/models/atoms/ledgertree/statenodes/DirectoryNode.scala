package com.odenzo.ripple.models.atoms.ledgertree.statenodes
import io.circe.syntax._
import cats.Show
import io.circe.generic.extras.Configuration
import io.circe._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.atoms.{AccountAddr, UInt64, RippleHash}
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * https://xrpl.org/directorynode.html
  * There are two types of directory nodes,.
  * Complicated by DirectoryID for Offers, lower 64 bits to taker pays amount / taker gets amount
  * Can disambiguate based on if Owner field is present its a OwnerDirectory type
  */
sealed trait DirectoryNode extends LedgerNode

object DirectoryNode {
  implicit val decode: Decoder[DirectoryNode] = Decoder.instance { hc =>
    hc.get[String]("Owner") match {
      case Right(owner) => hc.as[OwnerDirectoryNode]
      case _            => hc.as[OfferDirectoryNode]
    }
  }

  implicit val encoder = Encoder.AsObject.instance[DirectoryNode] {
    case v: OfferDirectoryNode => v.asJsonObject
    case v: OwnerDirectoryNode => v.asJsonObject
  }
}

/**
  * Directory Node, forgot what I was doing here, are there more than one type?
  * This shoudl handle all with  LedgerEntryType = DirectoryNode
  *
  * @param rootIndex
  * @param indexes
  * @param indexNext
  * @param indexPrev
  */
case class OfferDirectoryNode(
    //   exchangeRate deprecated
    flags: Long,
    indexes: List[LedgerNodeIndex],
    rootIndex: RippleHash,
    indexNext: Option[UInt64],
    indexPrev: Option[UInt64],
    takerPaysCurrency: String, // Hash160 of the currency
    takerGetsCurrency: String, // Hash160
    takerGetsIssuer: String    // Hash160
) extends DirectoryNode

object OfferDirectoryNode {
  implicit val config: Configuration                       = CirceCodecUtils.configCapitalizeExcept(Set("index"))
  implicit val decoder: Codec.AsObject[OfferDirectoryNode] = deriveConfiguredCodec[OfferDirectoryNode]
  implicit val show: Show[OfferDirectoryNode]              = Show.fromToString[OfferDirectoryNode]
}

/**
  * Directory Node,
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
    indexNext: Option[UInt64],
    indexPrev: Option[UInt64],
    index: Option[RippleHash]
) extends DirectoryNode

object OwnerDirectoryNode {
  implicit val config: Configuration                       = CirceCodecUtils.configCapitalizeExcept(Set("index"))
  implicit val decoder: Codec.AsObject[OwnerDirectoryNode] = deriveConfiguredCodec[OwnerDirectoryNode]
  implicit val show: Show[OwnerDirectoryNode]              = Show.fromToString[OwnerDirectoryNode]
}
