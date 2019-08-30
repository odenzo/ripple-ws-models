package com.odenzo.ripple.models.atoms.ledgertree.transactions

import scala.reflect.ClassTag

import io.circe.Decoder.Result
import io.circe.{DecodingFailure, Decoder, HCursor, _}
import io.circe.syntax._
import scribe.Logging

import com.odenzo.ripple.models.atoms.RippleTxnType._
import com.odenzo.ripple.models.atoms._
import cats._
import cats.data._
import cats.implicits._
import io.circe.generic.semiauto.deriveEncoder

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{PaymentTx, AccountSetTx}

/*
 * These are the messages that exist in account_tx responses, inside transaction array
 *  and tx responses. Not used for things in the Meta OR ledger browsing.
 *  They are very similar to the Tx json reponse in submission though. Elaborate and unify, including common fields.
 *  REference Ripple source code for common fields.
 *  UNDER RECONSTRUCTION
 */

/** Represent an expanded transaction is a ledger (typically validated)
  * The txn is at the top level and needs to be lifted, metaData is a top field. */
case class LedgerTxn(tx: RippleTransaction, common: TxCommon)

object LedgerTxn {
  implicit def decoder: Decoder[LedgerTxn] = Decoder.instance { hcursor =>
    for {
      txn <- hcursor.as[RippleTransaction]
      c   <- hcursor.as[TxCommon]
    } yield LedgerTxn(txn, c)
  }

  implicit val encoder: Encoder.AsObject[LedgerTxn] = Encoder.AsObject.instance[LedgerTxn] { ltxn =>
    val tx: JsonObject  = ltxn.tx.asJsonObject
    val com: JsonObject = ltxn.common.asJsonObject
    val fields          = tx.toList ::: com.toList
    JsonObject.fromIterable(fields)
  }

}

/** Represents a list of Ledger Tranasction that is either expanded or a */
case class LedgerTransactions(either: Either[List[LedgerTxn], List[LedgerNodeIndex]])

object LedgerTransactions {

  // Our cursor is on the Array of transactions. Each transaction is a LedgerNodeIndex or LedgerTxn
  implicit val decoder: Decoder[LedgerTransactions] = Decoder[List[LedgerTxn]]
    .either(Decoder[List[LedgerNodeIndex]])
    .map(LedgerTransactions.apply)

  implicit val encoder: Encoder[LedgerTransactions] = new Encoder[LedgerTransactions] {
    override final def apply(a: LedgerTransactions): Json = a.either match {
      case Left(l)  => l.asJson
      case Right(l) => l.asJson
    }
  }

}
