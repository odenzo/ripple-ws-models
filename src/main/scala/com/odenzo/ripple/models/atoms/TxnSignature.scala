package com.odenzo.ripple.models.atoms

import io.circe.Codec
import io.circe.generic.extras.semiauto._

/**
  * Simple wrapping now. This is a crypto signature on each txn for verification.
  * DER Encoded
  * @param v  142 character Hex string it seems.
  */
case class TxnSignature(v: String)

object TxnSignature {
  implicit val codec: Codec[TxnSignature] = deriveUnwrappedCodec[TxnSignature]
}
