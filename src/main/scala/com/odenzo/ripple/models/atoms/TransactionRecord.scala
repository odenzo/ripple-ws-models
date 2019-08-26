package com.odenzo.ripple.models.atoms

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
  * Models an individual transaction in the list of transactions returned by account_tx.
  * Never actually seen a LedgerIndex as documented at:   https://ripple.com/build/rippled-apis/#account-tx (scroll
  * down a bit)
  *
  *  TODO: Perhaps TxNode can be replced by RippleTranasction (sealed)
  *  LedgerIndex is optional, AccountTx doesn't return.  even in TxNode?
  */
case class TransactionRecord(ledger_index: Option[LedgerSequence], meta: Meta, tx: TxNode, validated: Boolean)

object TransactionRecord {

  implicit val decoder: Decoder[TransactionRecord] = deriveDecoder[TransactionRecord]
}
