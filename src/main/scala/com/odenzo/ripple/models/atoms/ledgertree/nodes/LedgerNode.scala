package com.odenzo.ripple.models.atoms.ledgertree.nodes

import cats._
import io.circe.{Decoder, Encoder, HCursor, JsonObject}

trait LedgerNode

/** This is designed to parse full LedgerNodes, e.g. from LedgerDataRs, not the deltas.
  * [[https://ripple.com/build/ledger-format/]] */
object LedgerNode {

  // TODO: Make LedgerEntryType an enumeration
  implicit val decoder: Decoder[LedgerNode] = Decoder.instance { (c: HCursor) =>
    c.get[String]("LedgerEntryType") match {
      case Right("AccountRoot")   => c.as[AccountRootNode]
      case Right("DirectoryNode") => c.as[DirectoryNode]
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
  implicit val encoder: Encoder[LedgerFields] = Encoder.encodeJsonObject.contramap[LedgerFields](n => n.fields)
  implicit val decoder: Decoder[LedgerFields] = Decoder.decodeJsonObject.map(LedgerFields.apply)

  implicit val show: Show[LedgerFields] = Show.show[LedgerFields](v => v.fields.asJson.spaces2)
}
