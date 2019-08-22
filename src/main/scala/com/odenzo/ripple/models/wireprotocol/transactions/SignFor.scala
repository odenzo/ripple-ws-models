package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Json, _}

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{PendingTxData, RippleTransaction}

/**
  * [[https://ripple.com/build/rippled-apis/#sign]]
  *
  * @param account   The account (which doesn't have to me initialized) or the multi-signer key
  * @param tx_json   The actual transaction data, must have SigningPubKey blank
  * @param seed_hex  master_seed_hex of the multisigner, either master or regular key (I think)
  * @param key_type  Type of Key (ed25519 or secp256k1)
  * @param id
  */
case class SignForRq(
    account: AccountAddr,
    tx_json: JsonObject,
    seed_hex: RippleSeedHex,
    key_type: RippleKeyType,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq

/**
  * Ultimately this should/could use SignRs?
  *
  */
case class SignForRs(tx_blob: TxBlob, signed: PendingTxData, tx_json: RippleTransaction) extends RippleRs

object SignForRq {

  val command: (String, Json) = "command" -> Json.fromString("sign_for")

  implicit val encoder: Encoder.AsObject[SignForRq] = {
    deriveEncoder[SignForRq].mapJsonObject(o => command +: o)
  }
}

object SignForRs {
  //implicit val decoder: Decoder[SignRs] = deriveDecoder[SignRs]

  implicit val decoder2: Decoder[SignForRs] = Decoder.instance { c =>
    for {
      txblob <- c.get[TxBlob]("tx_blob")
      signed <- c.get[PendingTxData]("tx_json")
      txrs   <- c.get[RippleTransaction]("tx_json")

    } yield SignForRs(txblob, signed, txrs)
  }
}
