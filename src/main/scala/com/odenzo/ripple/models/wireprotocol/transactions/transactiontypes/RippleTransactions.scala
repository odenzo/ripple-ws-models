package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.syntax._
import io.circe.{Encoder, Decoder, JsonObject}
import monocle.Lens
import monocle.macros.GenLens

import com.odenzo.ripple.models.atoms._

/**
  * Ripple transactions are signed and submitted based on a tx_json object containing the transaction details.
  * These are encoded in [TransactionName]TxRq case classes. All of which extend this trait.
  * These are encoded for a SignRq and the tx_json of SignRs has with SigningPubKey, TxnSignature, hash... added. Other
  * fields auto-filled (e.g. Fee, Sequence)
  * The SubmitRs has the SignRq tx_json in its response supplemented as in the SignRs
  * The TxRs (used to check transaction validation status) has in its `result` field the same as tx_json in submit
  * supplemented with inLedger, ledger_index (dup), date, validated=true (if so). And the meta which is different
  * altogether.
  *
  *
  *  Now Moving to having RippleTransaction seperate from TxOptions and TxCommonRs But adding flags in to each
  * Note that all Tx now execept PaymentChannelClaimTx have an account field and require a TxnSequence
  *  For now I do the autofill based on the JsonObject perhaps, or I could tag as OffLedger
  */
trait RippleTransaction {}

object RippleTransaction {

  implicit val decoder: Decoder[RippleTransaction] = Decoder.instance[RippleTransaction] { c =>
    import com.odenzo.ripple.models.atoms.RippleTxnType._
    c.get[RippleTxnType]("TransactionType").flatMap {
      case AccountSet           => c.as[AccountSetTx]
      case Payment              => c.as[PaymentTx]
      case EscrowFinish         => c.as[EscrowFinishTx]
      case EscrowCreate         => c.as[EscrowCreateTx]
      case EscrowCancel         => c.as[EscrowCancelTx]
      case TrustSet             => c.as[TrustSetTx]
      case OfferCreate          => c.as[OfferCreateTx]
      case OfferCancel          => c.as[OfferCancelTx]
      case SetRegularKey        => c.as[SetRegularKeyTx]
      case PaymentChannelCreate => c.as[PaymentChannelCreateTx]
      case PaymentChannelFund   => c.as[PaymentChannelFundTx]
      case PaymentChannelClaim  => c.as[PaymentChannelClaimTx]
      case SignerListSet        => c.as[SignerListSetTx]
    }
  }

  implicit val encoder: Encoder.AsObject[RippleTransaction] = Encoder.AsObject[RippleTransaction] {
    case tx: AccountSetTx           => tx.asJsonObject
    case tx: PaymentTx              => tx.asJsonObject
    case tx: TrustSetTx             => tx.asJsonObject
    case tx: EscrowCreateTx         => tx.asJsonObject
    case tx: EscrowCancelTx         => tx.asJsonObject
    case tx: EscrowFinishTx         => tx.asJsonObject
    case tx: SetRegularKeyTx        => tx.asJsonObject
    case tx: OfferCreateTx          => tx.asJsonObject
    case tx: OfferCancelTx          => tx.asJsonObject
    case tx: PaymentChannelCreateTx => tx.asJsonObject
    case tx: PaymentChannelFundTx   => tx.asJsonObject
    case tx: PaymentChannelClaimTx  => tx.asJsonObject
    case tx: SignerListSetTx        => tx.asJsonObject
  }

}

/**
  *  Some options to apply when constructing a RippleTransaction Request (e.g. for Signing)
  * * This corresponds to standard fields in a transaction that has been signed OR submitted, but not yet validated.
  * * Used to build and submit transactions
  *
  * @param fee  Fee to use, alternative would be to let Ripple server do it, but not good with local signing anyway
  * @param memos
  * @param sequence Same as Fee, this should always be set manually now, but for transition None will let sewrver
  *                 autofill when signing on server
  * @param lastLedgerSequence
  */
//@Lenses
case class TxOptions(
    fee: Drops = Drops.stdFee, // Rq / Rs
    sequence: Option[TxnSequence] = None,
    accountTxnID: Option[Hash256] = None,
    lastLedgerSequence: LedgerSequence = LedgerSequence.MAX, // Instead of option. Should always be set reasonably.
    memos: Option[Memos] = None,                             // Rq / Rs
    sourceTag: Option[UInt32] = None
)

object TxOptions {

  val lensFee: Lens[TxOptions, Drops]                    = GenLens[TxOptions](_.fee)
  val lensMemos: Lens[TxOptions, Option[Memos]]          = GenLens[TxOptions](_.memos)
  val lensSequence: Lens[TxOptions, Option[TxnSequence]] = GenLens[TxOptions](_.sequence)
  val lensLastSeq: Lens[TxOptions, LedgerSequence]       = GenLens[TxOptions](_.lastLedgerSequence)

  implicit val encoder: Encoder.AsObject[TxOptions] = Encoder.AsObject.instance { v =>
    JsonObject(
      "Fee"                := v.fee,
      "Sequence"           := v.sequence,
      "AccountTxnID"       := v.accountTxnID,
      "LastLedgerSequence" := v.lastLedgerSequence,
      "Memos"              := v.memos,
      "SourceTag"          := v.sourceTag
    )

  }
  implicit val decoder: Decoder[TxOptions] =
    Decoder.forProduct6("Fee", "Sequence", "AccountTxnID", "LastLedgerSequence", "Memos", "SourceTag")(TxOptions.apply)
}

// @Lenses compile error.
case class TxSigningInfo(
    signingPublicKey: SigningPublicKey,
    signers: Option[Signers],
    txnSignature: Option[TxnSignature]
)

/**
  * This corresponds to standard fields in a transaction that has been signed OR submitted, but not yet validated.
  * @param signingPubKey
  * @param txnSignature  Optional so we can deal with multi-sig result too.
  * @param hash
  * @param txnType
  */
case class PendingTxData(
    signingPubKey: SigningPublicKey,
    txnSignature: Option[TxnSignature],
    signers: Option[Signers], // Can be empty? Can be null?
    hash: TxnHash,
    txnType: RippleTxnType,
    fee: Option[Drops] = None,   // Rq / Rs
    memos: Option[Memos] = None, // Rq / Rs
    sequence: Option[TxnSequence] = None,
    lastLedgerSequence: Option[LedgerSequence] = None // Multiple ways of setting, use Ledger instead!
)

object PendingTxData {

  implicit val decoder: Decoder[PendingTxData] =
    Decoder.forProduct9(
      "SigningPubKey",
      "TxnSignature",
      "Signers",
      "hash",
      "TransactionType",
      "Fee",
      "Memos",
      "Sequence",
      "LastLedgerSequence"
    )(PendingTxData.apply)

}

/** This is EXTRA data (on top of PendingTxData) I believe this is here is closed or validated transactions. Not
  * positive about closed */
case class ValidatedTxData(ledgerIndex: LedgerSequence, date: RippleTime)

object ValidatedTxData {

  implicit val decoder: Decoder[ValidatedTxData] = Decoder.forProduct2("ledger_index", "date")(ValidatedTxData.apply)

}
