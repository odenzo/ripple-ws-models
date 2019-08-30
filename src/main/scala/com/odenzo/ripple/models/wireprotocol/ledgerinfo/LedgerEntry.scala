package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.statenodes.AccountRootNode
import com.odenzo.ripple.models.support.{RippleScrollingRq, RippleScrollingRs}
import com.odenzo.ripple.models.utils.CirceCodecUtils

case class LedgerEntryRq(
    tipe: String = "account_root",
    account_root: Option[AccountAddr] = None,
    ledger: LedgerID = LedgerName.VALIDATED_LEDGER,
    index: Option[String] = None, // Not a LedgerIndex, a LedgerNodeIndex
    binary: Boolean = false,
    limit: Limit = Limit.default,
    marker: Option[Marker] = None,
    id: RippleMsgId = RippleMsgId.random
) extends RippleScrollingRq

case class LedgerEntryRs(
    ledger_index: LedgerSequence,
    index: String,
    node: Option[AccountRootNode],
    node_binary: Option[String],
    marker: Option[Marker]
) extends RippleScrollingRs

// LedgerAccept and LedgerRequest skipped. I don't use most of these.

object LedgerEntryRq extends CirceCodecUtils {
  implicit val configuration: Configuration = Configuration.default.withDefaults
  implicit val encoder: Encoder.AsObject[LedgerEntryRq] =
    deriveConfiguredEncoder[LedgerEntryRq]
      .mapJsonObject(withCommandAndLedgerID("account_objects") andThen withRenameField("tipe", "type"))

}

object LedgerEntryRs {
  implicit val config: Configuration           = Configuration.default
  implicit val decoder: Decoder[LedgerEntryRs] = deriveConfiguredDecoder[LedgerEntryRs]

}

object Debug {
  case class Foo(
      //tipe: String = "account_root",
      // account_root: Option[AccountAddr] = None,
      // ledger: LedgerID = LedgerName.VALIDATED_LEDGER,
      // index: Option[String] = None, // Not a LedgerIndex, a LedgerNodeIndex
      // binary: Boolean = false,
      //limit: Limit = Limit.default,
      //marker: Option[Marker] = None,
      //id: RippleMsgId = RippleMsgId.random
      msg: String
  )

  object Foo {
    implicit val configuration: Configuration = Configuration.default
    implicit val encoder: Encoder.AsObject[Foo] =
      deriveConfiguredEncoder[Foo]
  }
}
