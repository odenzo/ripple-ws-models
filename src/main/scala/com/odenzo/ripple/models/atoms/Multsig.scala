package com.odenzo.ripple.models.atoms

import scala.collection.immutable

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import io.circe.syntax._
import io.circe.{JsonObject, Encoder, Decoder, Codec, Json}

import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.TrustSetTx

/**
  * Used for the actual signing of a transaction.
  *
  * @param account
  * @param signingPubKey
  * @param txnSignature
  */
case class Signer(account: AccountAddr, signingPubKey: RipplePublicKey, txnSignature: TxnSignature)

object Signer {
  implicit val config: Configuration         = CirceCodecUtils.capitalizeConfiguration
  implicit val codec: Codec.AsObject[Signer] = deriveConfiguredCodec[Signer]
}

/** The signers appear in tx_json elements (RippleTransaction in via CommonTx).
  * Signer is array os JsonObjects each with exactly one Signer object.
  * Used for the actual (multi) signing of a transaction.
  * @param signers
  */
case class Signers(signers: List[Signer])

object Signers {
  import io.circe.generic.semiauto._
  implicit val encoder: Encoder[Signers] = Encoder.instance[Signers] { v =>
    val objects: immutable.Seq[JsonObject] = v.signers.map(s => JsonObject.singleton("Signer", s.asJson))
    val json: Json                         = objects.asJson
    json
  }

  private case class Dummy(Signer: Signer)
  //noinspection ScalaUnusedSymbol
  private object Dummy { implicit val decoder: Decoder[Dummy] = deriveDecoder[Dummy] }

  implicit val decoder: Decoder[Signers] = {
    val sub: Decoder[List[Signer]] = Decoder
      .decodeList[Dummy]
      .map(list => list.map(dummy => dummy.Signer))

    sub.map(Signers(_))

  }
}

/**
  * This is used in the SignerListSet to add signers/quorom to an account.
  * @param account    The address of the signing account keys
  * @param signerWeight The weight that this signer entry contributes to quorum
  */
case class SignerEntry(account: AccountAddr, signerWeight: Int)

/** The correct encoding and decoding within nested JsonObject is handled here */
object SignerEntry {

  implicit val config: Configuration              = CirceCodecUtils.capitalizeConfiguration
  implicit val codec: Codec.AsObject[SignerEntry] = deriveConfiguredCodec[SignerEntry]

}
