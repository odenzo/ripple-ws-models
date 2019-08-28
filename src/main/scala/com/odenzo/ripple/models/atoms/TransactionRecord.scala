package com.odenzo.ripple.models.atoms

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

import com.odenzo.ripple.models.atoms.ledgertree.transactions.LedgerTransaction

/**
  * Models an individual transaction in the list of transactions returned by account_tx.
  * Never actually seen a LedgerIndex as documented at:   https://ripple.com/build/rippled-apis/#account-tx (scroll
  * down a bit)
  *
  *  I think this is extra. LedgerTxn should be able to handle.
  */
case class TransactionRecord(
    ledger_index: Option[LedgerSequence],
    meta: Meta,
    tx: LedgerTransaction,
    validated: Boolean
)

object TransactionRecord {

  implicit val decoder: Decoder[TransactionRecord] = deriveDecoder[TransactionRecord]
}
