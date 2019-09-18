package com.odenzo.ripple.models.atoms.ledgertree.transactions

import com.odenzo.ripple.models.utils.CirceCodecUtils
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec
import cats._
import cats.data._
import cats.implicits._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.TxnStatusCode
import com.odenzo.ripple.models.wireprotocol.txns.RippleTxnType

/** Common fields *extracts* from LedgerRs.result.ledger.tranasactions example
  * Not sure where to put owner_funds which is for OfferCreateTx but only in ledger */
case class LedgerTxCommon(
    fee: Drops,
    lastLedgerSequence: Option[LedgerSequence], // Depends on submission of request
    memos: Option[Memos],
    sequence: TxnSequence,
    signingPubKey: Option[SigningPublicKey], // Actually this is either Signers or a signingPubKey
    signers: Option[Signers],
    transactionType: RippleTxnType,
    txnSignature: Option[TxnSignature],
    metaData: Option[Meta], // meta sometimes metaData if sourced from LedgerTxn though
    meta: Option[Meta],     // meta is sourced from TxRq() :-(
    ledger_index: Option[LedgerSequence],
    date: Option[RippleTime], // Only if validated when using TxRs
    hash: Option[TxnHash],
    validated: Option[Boolean]
) {

  def transactionResult: Option[TxnStatusCode] = this.metaData.map(_.transactionResult)
}

object LedgerTxCommon {
  implicit val config: Configuration =
    CirceCodecUtils.configCapitalizeExcept(Set("hash", "date", "meta", "validated", "ledger_index")).withDefaults

  // Well, I think if we use an unwrapped codec (a) It will use config and (b) it will practically lift
  // so  X(a:A,b:B, date:RippleTime, validated:Boolen) with we autoderive X(a:A,b:B, txcommon:TxCommon)
  implicit val codec: Codec.AsObject[LedgerTxCommon] = deriveConfiguredCodec[LedgerTxCommon]

  /** Dummy TxCommon for development hacking. */
  val dummy = LedgerTxCommon(
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
    None,
    TxnHash("garbage").some,
    None
  )
}
