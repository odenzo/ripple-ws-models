package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.{CommonTxnRs, RippleTxnRq}
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRq, RippleRs}
import com.odenzo.ripple.models.wireprotocol.txns.RippleTx

/**
  * [[https://ripple.com/build/rippled-apis/#sign]]
  *
  * Secret, seed, passphrase style not supported for simplicity. See ripple-local-signing for conversion routines.

  * @param tx_json   Transaction data to sign.
  * @param account   The account (which doesn't have to me initialized) or the multi-signer key
  * @param seed_hex  Hex signing key
  * @param key_type  Type of Key (ed25519 or secp256k1)
  * @param id
  */
case class SignForRq(
    tx_json: RippleTxnRq,
    account: AccountAddr,
    seed_hex: RippleSeedHex,
    key_type: RippleKeyType,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq

/**
  * Ultimately this should/could use SignRs?
  *
  */
case class SignForRs(tx_blob: TxBlob, signed: CommonTxnRs, tx_json: RippleTx) extends RippleRs

object SignForRq extends CirceCodecUtils {
  private type ME = SignForRq
  private val command: String            = "sign_for"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)

}

object SignForRs {

  implicit val decoder2: Decoder[SignForRs] = Decoder.instance { c =>
    for {
      txblob <- c.get[TxBlob]("tx_blob")
      signed <- c.get[CommonTxnRs]("tx_json")
      txrs   <- c.get[RippleTx]("tx_json")

    } yield SignForRs(txblob, signed, txrs)
  }

  implicit val encoder: Encoder.AsObject[SignForRs] = Encoder.AsObject.instance[SignForRs] { a =>
    val fields = ("tx_blob" := a.tx_blob) :: a.signed.asJsonObject.toList ::: a.tx_json.asJsonObject.toList
    JsonObject.fromIterable(fields)
  }

}
