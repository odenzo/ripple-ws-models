package com.odenzo.ripple.models.wireprotocol

import monocle.macros.Lenses

import com.odenzo.ripple.models.atoms._

/** This can be used to pass around information to sign a trasanction */
trait TransactionAuth

case class SingleSigned(
    signingPublicKey: SigningPublicKey = SigningPublicKey(""),
    txnSignature: TxnSignature
) extends TransactionAuth

@Lenses("_")
case class MultiSigned(signers: Signers, signingPublicKey: SigningPublicKey = SigningPublicKey(""))
    extends TransactionAuth
