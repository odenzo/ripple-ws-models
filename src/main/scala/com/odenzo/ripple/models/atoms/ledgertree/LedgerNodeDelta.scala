package com.odenzo.ripple.models.atoms.ledgertree

import scala.collection.immutable.Nil

import io.circe._
import io.circe.generic.extras._
import io.circe.generic.semiauto._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.LedgerSequence
import com.odenzo.ripple.models.atoms.ledgertree.statenodes._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * There is really only one of these, modified, created -- no deleted?
  * @param modifiedNode Node in ledger that has been changed
  * @param createdNode A new node in the ledger
  */
case class AffectedLedgerNode(modifiedNode: Option[LedgerNodeDelta], createdNode: Option[LedgerNodeDelta]) {}

object AffectedLedgerNode {

  import io.circe.generic.extras._
  import io.circe.generic.extras.semiauto._
  import io.circe.generic.extras.{semiauto => genextras}
  implicit val circeConfig: Configuration                = CirceCodecUtils.capitalizeConfig
  implicit val codec: Codec.AsObject[AffectedLedgerNode] = genextras.deriveConfiguredCodec[AffectedLedgerNode]
}

/**
  * Base for containers like CreatedNode and ModifiedNode that are found in AccountTx
  * NOTE THIS IS USING DIFFERENT DECODERS FROM CIRCE_DERIVATION
  */
// For Dev! This is any of Created, Modified type now. For any type of node record (e.g. AccountRoot, RippleState)
case class LedgerNodeDelta(
    ledgerEntryType: String,                   //LedgerEntryType, // String
    ledgerIndex: LedgerNodeIndex,              // 64-char Hex
    previousTxnID: Option[LedgerNodeIndex],    // Define?
    previousTxnLgrSeq: Option[LedgerSequence], // The ledger the last transaction was in
    finalFields: Option[JsonObject],
    previousFields: Option[JsonObject],
    newFields: Option[JsonObject]
)

object LedgerNodeDelta {

  import io.circe.generic.extras._
  import io.circe.generic.extras.semiauto._
  import io.circe.generic.extras.{semiauto => genextras}
  implicit val circeConfig: Configuration             = CirceCodecUtils.capitalizeConfig
  implicit val codec: Codec.AsObject[LedgerNodeDelta] = genextras.deriveConfiguredCodec[LedgerNodeDelta]

}

/** Start out with Object blob, move to Shapeless later */
case class DeltaFields[A <: LedgerNode](fields: JsonObject)
