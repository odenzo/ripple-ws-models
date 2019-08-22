package com.odenzo.ripple.models.atoms

import scala.collection.immutable

import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json, JsonObject}

/**
  * Used for the actual signing of a transaction.
  *
  * @param account
  * @param signingPubKey
  * @param txnSignature
  */
case class Signer(account: AccountAddr, signingPubKey: RipplePublicKey, txnSignature: TxnSignature)

object Signer {

  implicit val encoder: Encoder[Signer] =
    Encoder.forProduct3("Account", "SigningPubKey", "TxnSignature")(v => (v.account, v.signingPubKey, v.txnSignature))

  implicit val decoder: Decoder[Signer] = Decoder.forProduct3("Account", "SigningPubKey", "TxnSignature")(Signer.apply)

}

/** The signers appear in tx_json elements (RippleTransaction in via CommonTx).
  * Signer is array os JsonObjects each with exactly one Signer object.
  * Used for the actual (multi) signing of a transaction.
  * @param signers
  */
case class Signers(signers: List[Signer])

object Signers {

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
  * @param account
  * @param signerWeight
  */
case class SignerEntry(account: AccountAddr, signerWeight: Int)

/** The correct encoding and decoding within nested JsonObject is handled here */
object SignerEntry {
  implicit val decode: Decoder[SignerEntry] = {
    Decoder.forProduct2("Account", "SignerWeight")(SignerEntry.apply).prepare(_.downField("SignerEntry"))
  }

  implicit val encoder: Encoder[SignerEntry] = {
    val base: Encoder.AsObject[SignerEntry] =
      Encoder.forProduct2("Account", "SignerWeight")(v => (v.account, v.signerWeight))
    base.mapJsonObject(base => JsonObject.singleton("SignerEntry", base.asJson))
  }

}
