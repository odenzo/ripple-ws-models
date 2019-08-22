package com.odenzo.ripple.models.atoms.ledgertree

import scala.collection.immutable.Nil

import io.circe._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.LedgerSequence
import com.odenzo.ripple.models.atoms.ledgertree.nodes._

/**
  * There is really only one of these, modified, created -- no deleted?
  * @param modifiedNode Node in ledger that has been changed
  * @param createdNode A new node in the ledger
  */
case class AffectedLedgerNode(modifiedNode: Option[LedgerNodeDelta], createdNode: Option[LedgerNodeDelta]) {}

object AffectedLedgerNode {
  implicit val decoder: Decoder[AffectedLedgerNode] = {
    Decoder.forProduct2("ModifiedNode", "CreatedNode")(AffectedLedgerNode.apply)
  }
}

/**
  * Base for containers like CreatedNode and ModifiedNode that are found in AccountTx
  * NOTE THIS IS USING DIFFERENT DECODERS FROM CIRCE_DERIVATION
  */
// For Dev! This is any of Created, Modified type now. For any type of node record (e.g. AccountRoot, RippleState)
case class LedgerNodeDelta(
    ledgerEntryType: String,      //LedgerEntryType, // String
    ledgerIndex: LedgerNodeIndex, // 64-char Hex
    previousTxnID: Option[LedgerNodeIndex],
    previousTxnLgrSeq: Option[LedgerSequence],
    finalFields: Option[JsonObject],
    previousFields: Option[JsonObject],
    newFields: Option[JsonObject]
)

object LedgerNodeDelta {

  implicit val decoder: Decoder[LedgerNodeDelta] = {
    Decoder.forProduct7(
      "LedgerEntryType",
      "LedgerIndex",
      "PreviousTxnID",
      "PreviousTxnLgrSeq",
      "FinalFields",
      "PreviousFields",
      "NewFields"
    )(LedgerNodeDelta.apply)
  }

  def debugDump(v: LedgerNodeDelta): String = {
    val r = s"LedgerNodeDelta Dump of type ${v.ledgerEntryType}" ::
      "Final Fields: " + v.finalFields.map(_.asJson.spaces2).getOrElse("No Final Fields") ::
      s"Ledger (Node) Index: ${v.ledgerIndex.v}" ::
      "FModified Fields: " + v.previousFields.map(_.asJson.spaces2).getOrElse("No PrevFields") ::
      "Created Fields: " + v.newFields.map(_.asJson.spaces2).getOrElse("No Created Fields") ::
      Nil

    r.mkString("\n")
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

/** Start out with Object blob, move to Shapeless later */
case class DeltaFields[A <: LedgerNode](fields: JsonObject)
