package com.odenzo.ripple.models.atoms

import io.circe.{Decoder, Encoder}

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
  implicit val encoder: Encoder[TxnIndex] = Encoder.encodeLong.contramap[TxnIndex](_.v)
  implicit val decoder: Decoder[TxnIndex] = Decoder.decodeLong.map(TxnIndex(_))
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
  implicit val encoder: Encoder[TxnSequence] = Encoder.encodeLong.contramap[TxnSequence](_.v)
  implicit val decoder: Decoder[TxnSequence] = Decoder.decodeLong.map(TxnSequence(_))

}

case class OfferSequence(v: Long)

object OfferSequence {
  implicit val encoder: Encoder[OfferSequence] = Encoder.encodeLong.contramap[OfferSequence](_.v)
  implicit val decoder: Decoder[OfferSequence] = Decoder.decodeLong.map(OfferSequence(_))

}
