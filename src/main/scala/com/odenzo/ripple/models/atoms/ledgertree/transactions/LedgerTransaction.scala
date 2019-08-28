package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.Decoder.Result
import io.circe.{DecodingFailure, Decoder, HCursor, _}
import io.circe.generic.extras.Configuration
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
import scribe.Logging

import com.odenzo.ripple.models.atoms.RippleTxnType._
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import cats._
import cats.data._
import cats.implicits._

/*
 * These are the messages that exist in account_tx responses, inside transaction array
 *  and tx responses. Not used for things in the Meta OR ledger browsing.
 *  They are very similar to the Tx json reponse in submission though. Elaborate and unify, including common fields.
 *  REference Ripple source code for common fields.
 *  UNDER RECONSTRUCTION
 */

/** Common fields extracts from LedgerRs.result.ledger.tranasactions example */
case class TxCommon(
    fee: Drops,
    lastLedgerSequence: Option[LedgerSequence], // Depends on submission of request
    memos: Option[Memos],
    sequence: TxnSequence,
    signingPubKey: Option[RipplePublicKey], // Actually this is either Signers or a signingPubKey
    signers: Option[Signers],
    transactionType: RippleTxnType,
    txnSignature: Option[TxnSignature],
    metaData: Option[Meta],
    date: Option[RippleTime], // Only if validated when using TxRs     // This is not in LedgerRs.result.ledget
    // .transactions
    hash: TxnHash
)

object TxCommon {
  implicit val config: Configuration           = CirceCodecUtils.capitalizeExcept(Set("hash", "date", "metaData"))
  implicit val codec: Codec.AsObject[TxCommon] = deriveConfiguredCodec[TxCommon]

  /** Dummy TxCommon for development hacking. */
  val dummy = TxCommon(
    Drops(666),
    None,
    None,
    TxnSequence(666),
    RipplePublicKey(Base58Checksum("garbage")).some,
    None,
    RippleTxnType.EscrowFinish,
    TxnSignature("garbage").some,
    None,
    None,
    TxnHash("garbage")
  )
}

/** A transaction that is stored/sourced from the ledger (open,closed or validated ledgers technically) */
trait LedgerTransaction {
  val common: TxCommon
}

object LedgerTransaction extends Logging {
  implicit val rootDecoder: Decoder[LedgerTransaction] = new Decoder[LedgerTransaction] {
    override def apply(c: HCursor): Result[LedgerTransaction] = {

      val txnType = c.get[RippleTxnType]("TransactionType")
      logger.info(s"Decodeing a LedgerTransaction of type $txnType")
      txnType.flatMap {
        //case TrustSet => c.as[TxTrustSet]
        case Payment    => c.as[TxPayment]
        case AccountSet => c.as[TxAccountSet]
//        case SetRegularKey => c.as[TxSetRegularKey]
//        case EscrowCreate  => c.as[TxEscrowCreate]
//        case EscrowCancel  => c.as[TxEscrowCancel]
//        case OfferCreate   => c.as[TxOfferCreate]
//        case OfferCancel   => c.as[TxOfferCancel]
        case other =>
          Left(DecodingFailure(s"Unknown TransactionType [$other]", c.history)) // FIXME: This is not surviving
      }
    }
  }

  implicit val rootEncoder: Encoder[LedgerTransaction] = new Encoder[LedgerTransaction] {
    override def apply(a: LedgerTransaction): Json = a match {

      case n: TxPayment => n.asJson
      case other        => s"TxNode Root Encoder Note Setup for ${other}".asJson
    }
  }
}
