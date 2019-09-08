package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.syntax._
import io.circe.{Encoder, Decoder}

import com.odenzo.ripple.models.wireprotocol.txns.RippleTxnType._

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
trait RippleTx

// See RippleTxnType enumeration...
object RippleTx {

  implicit val decoder: Decoder[RippleTx] = Decoder.instance[RippleTx] { c =>
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

  implicit val encoder: Encoder.AsObject[RippleTx] = Encoder.AsObject[RippleTx] {
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
