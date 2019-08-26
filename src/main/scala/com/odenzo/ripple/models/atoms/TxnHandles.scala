package com.odenzo.ripple.models.atoms

import io.circe.{Decoder, Encoder}
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

/**
  * Re-organizig to put all the ways you can identify a transactions
  * here.
  * Will go find TxnHash and put here too.
  * LedgerNode ID I think will go in seperate file grouping on that concept.
  *
  */
trait TxnHandles

/**
  * This stores the index of a transaction in a ledger.
  * So, the 50th transaction for an account in a new ledger might have an index of 4
  * But TxnSequence would be 50.
  **/
case class TxnIndex(v: Long) extends AnyVal()

object TxnIndex {
  implicit val codec: Codec[TxnIndex] = deriveUnwrappedCodec[TxnIndex]

}

/**
  * This is the sequence of a transaction local to the "on-account" (which signs the txn)
  * Give the account and TxnSequence can find.
  * Name in JSON is usually (?!) Sequence
  *
  **/
case class TxnSequence(v: Long) {

  /** Returns new TxnSequence with incremented value */
  def increment = TxnSequence(v + 1)
}

object TxnSequence {
  implicit val codec: Codec[TxnSequence] = deriveUnwrappedCodec[TxnSequence]

}

case class OfferSequence(v: Long)

object OfferSequence {
  implicit val codec: Codec[OfferSequence] = deriveUnwrappedCodec[OfferSequence]

}
