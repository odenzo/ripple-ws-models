package com.odenzo.ripple.models.atoms

import io.circe.{Decoder, Encoder}

/**
  * Simple wrapping now. This is a crypto signature on each txn for verification.
  * DER Encoded
  * @param v  142 character Hex string it seems.
  */
case class TxnSignature(v: String)

// This is an example from Tx
//304502210085974FA1E3192621D4481EC635A15D8955842E4C551D2C6907CCE457D3B4E9EE022068B31369259CC08F25BB452A6D7CCE3BF0231D877B5C789EA8F79D0B79A3CF8C

object TxnSignature {

  implicit val encoder: Encoder[TxnSignature] = Encoder.encodeString.contramap[TxnSignature](_.v)
  implicit val decoder: Decoder[TxnSignature] = Decoder.decodeString.map(TxnSignature(_))

}
