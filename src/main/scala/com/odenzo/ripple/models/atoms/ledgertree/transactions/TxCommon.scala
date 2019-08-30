package com.odenzo.ripple.models.atoms.ledgertree.transactions

import com.odenzo.ripple.models.utils.CirceCodecUtils
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.{Codec, Decoder}
import cats._
import cats.data._
import cats.implicits._

import com.odenzo.ripple.models.atoms._

// TODO: See if we can reduce this to just one TxCommon for validated/current/closed scenarios

/** Common fields *extracts* from LedgerRs.result.ledger.tranasactions example */
case class TxCommon(
    fee: Drops,
    lastLedgerSequence: Option[LedgerSequence], // Depends on submission of request
    memos: Option[Memos],
    sequence: TxnSequence,
    signingPubKey: Option[SigningPublicKey], // Actually this is either Signers or a signingPubKey
    signers: Option[Signers],
    transactionType: RippleTxnType,
    txnSignature: Option[TxnSignature],
    metaData: Option[Meta],
    //inLedger and last_ledger_index that same, but both missing
    ledger_index: Option[LedgerSequence],
    date: Option[RippleTime], // Only if validated when using TxRs     // This is not in LedgerRs.result.ledget
    // .transactions
    hash: TxnHash,
    validated: Boolean = false
)

object TxCommon {
  implicit val config: Configuration =
    CirceCodecUtils.capitalizeExcept(Set("hash", "date", "metaData", "validated")).withDefaults
  implicit val codec: Codec.AsObject[TxCommon] = deriveConfiguredCodec[TxCommon]

  /** Dummy TxCommon for development hacking. */
  val dummy = TxCommon(
    Drops(666),
    None,
    None,
    TxnSequence(666),
    SigningPublicKey("").some,
    None,
    RippleTxnType.EscrowFinish,
    TxnSignature("garbage").some,
    None,
    None,
    None,
    TxnHash("garbage")
  )
}
