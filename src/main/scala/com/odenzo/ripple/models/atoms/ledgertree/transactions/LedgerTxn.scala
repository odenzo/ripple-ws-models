package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.syntax._
import io.circe.{Decoder, Encoder, JsonObject}

import com.odenzo.ripple.models.wireprotocol.txns.RippleTx

/** Represent an expanded transaction in a ledger. It is also used for transactions
  * contained in SignRs
  * The txn is at the top level and needs to be lifted, metaData is a top field. */
case class LedgerTxn(tx: RippleTx, common: LedgerTxCommon)

object LedgerTxn {

  // In the ledger TxCommon is overkill, validated is not present and set to false
  // Ledger_index is capitalized
  implicit def decoder: Decoder[LedgerTxn] = Decoder.instance { hcursor =>
    for {
      txn <- hcursor.as[RippleTx]
      c   <- hcursor.as[LedgerTxCommon]
    } yield com.odenzo.ripple.models.atoms.ledgertree.transactions.LedgerTxn(txn, c)
  }

  implicit val encoder: Encoder.AsObject[LedgerTxn] = Encoder.AsObject.instance[LedgerTxn] { ltxn =>
    val tx: JsonObject  = ltxn.tx.asJsonObject
    val com: JsonObject = ltxn.common.asJsonObject
    val fields          = tx.toList ::: com.toList
    JsonObject.fromIterable(fields)
  }

}
