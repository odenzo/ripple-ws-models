package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support

import io.circe.generic.extras.Configuration
import io.circe.syntax._
import io.circe.{Codec, Encoder, JsonObject, Decoder}
import monocle.Lens
import monocle.macros.GenLens

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes._

/** This can be used to pass around information to sign a trasanction */
trait TransactionAuth

case class SingleSigned(
    signingPublicKey: SigningPublicKey = SigningPublicKey(""),
    txnSignature: TxnSignature
) extends TransactionAuth

// @Lenses compile error.
case class MultiSigned(signers: Signers, signingPublicKey: SigningPublicKey = SigningPublicKey(""))
    extends TransactionAuth
