package com.odenzo.ripple.models.atoms

import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
import io.circe.generic.JsonCodec
import io.circe.generic.extras.ConfiguredJsonCodec
import io.circe.{Encoder, Decoder}

/**
  *
  * @param v Represents the transaction blob return from the sign command. This is Hex and should state so.
  *
  */
case class TxBlob(v: String) extends AnyVal

object TxBlob {
  implicit val codec: Codec[TxBlob] = deriveUnwrappedCodec[TxBlob]

}
