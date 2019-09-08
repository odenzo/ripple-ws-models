package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import cats._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe._

import com.odenzo.ripple.models.utils.CirceCodecUtils

trait LedgerNode

/** This is designed to parse full LedgerNodes, e.g. from LedgerDataRs, not the deltas.
  * [[https://ripple.com/build/ledger-format/]] */
object LedgerNode {

  // TODO: Make LedgerEntryType an enumeration
  implicit val decoder: Decoder[LedgerNode] = Decoder.instance { (c: HCursor) =>
    c.get[String]("LedgerEntryType") match {
      case Right("AccountRoot")   => c.as[AccountRootNode]
      case Right("DirectoryNode") => c.as[OfferDirectoryNode]
      case Right("Escrow")        => c.as[EscrowNode]
      case Right("Offer")         => c.as[OfferNode]
      case Right("PayChannel")    => c.as[PayChannelNode]
      case Right("RippleState")   => c.as[RippleStateNode]
      case Right("SignerList")    => c.as[SignerListNode]
      case Right("FeeSettings")   => c.as[FeeSettingsNode] // Exists, not in ledger_format doc.
      case Right("LedgerHashes")  => c.as[LedgerHashesNode] // Exists, not in ledger_format doc.
      case Right("Amendments")    => c.as[AmendmentsNode] // Exists, not in ledger_format doc.
      case failed =>
        Decoder.failedWithMessage(s"Discriminator Not Handled $failed")(c)
    }

  }

// FIXME: Forgot what I was doing here.
//private object Utils {
//
//  def ledgerNodeTypeToDecoder(ltype: String): Decoder[_ <: LedgerNode] = {
//
//    val decoder: Decoder[_ <: LedgerNode] = ltype match {
//      case "AccountRoot"   => Decoder[AccountRootNode]
//      case "RippleState"   => Decoder[RippleStateNode]
//      case "DirectoryNode" => Decoder[DirectoryNode]
//      case "Escrow"        => Decoder[EscrowNode]
//      case "Offer"         => Decoder[OfferNode]
//      case "SignerList"    => Decoder[SignerListNode]
//      case "FeeSettings"   => Decoder[FeeSettingsNode]
//      case "LedgerHashes"  => Decoder[LedgerHashesNode]
//      case "Amendments"    => Decoder[AmendmentsNode]
//      case failed =>
//        val failDecode: Decoder[LedgerNode] = Decoder
//          .failedWithMessage[LedgerNode](s"Discriminator Not Handled $failed")
//        failDecode
//    }
//    decoder
//  }
//}

  implicit val encoder: Encoder[LedgerNode] = Encoder.encodeString.contramap[LedgerNode](_ => "Not Yet")

  implicit val show: Show[LedgerNode] = Show.show[LedgerNode] { v =>
//    case n: DirectoryNodeEntry => n.show
//    case n: AccountData => n.show
    "Should Be Showing Ledger Entry Now"
  }

}

/** FIXME: Tempted to use Shapeless Record here instead, prototype but for use in DeltaNodes */
case class LedgerFields(fields: JsonObject)

object LedgerFields {
  import io.circe.syntax._
  implicit val config: Configuration               = CirceCodecUtils.configCapitalizeExcept()
  implicit val codec: Codec.AsObject[LedgerFields] = deriveConfiguredCodec[LedgerFields]
  implicit val show: Show[LedgerFields]            = Show.show[LedgerFields](v => v.fields.asJson.spaces2)
}
