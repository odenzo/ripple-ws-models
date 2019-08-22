package com.odenzo.ripple.models.atoms

import io.circe.{Decoder, Encoder}

/**
  *
  * @param v Represents the transaction blob return from the sign command. This is Hex and should state so.
  *
  */
case class TxBlob(v: String) extends AnyVal

object TxBlob {
  implicit val encoder: Encoder[TxBlob] = Encoder.encodeString.contramap[TxBlob](_.v)
  implicit val decoder: Decoder[TxBlob] = Decoder.decodeString.map(TxBlob(_))
}
