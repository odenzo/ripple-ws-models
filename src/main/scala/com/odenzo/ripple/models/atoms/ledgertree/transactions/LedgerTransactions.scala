package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.{Decoder, _}
import io.circe.syntax._
import cats._
import cats.data._
import cats.implicits._

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex

/*
 * These are the messages that exist in account_tx responses, inside transaction array
 *  and tx responses. Not used for things in the Meta OR ledger browsing.
 *  They are very similar to the Tx json reponse in submission though. Elaborate and unify, including common fields.
 *  REference Ripple source code for common fields.
 *  UNDER RECONSTRUCTION
 */

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
