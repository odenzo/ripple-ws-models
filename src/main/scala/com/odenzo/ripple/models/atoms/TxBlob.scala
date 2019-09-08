package com.odenzo.ripple.models.atoms

import io.circe._
import io.circe.generic.extras.semiauto._

/**
  *
  * @param v Represents the transaction blob return from the sign command. This is Hex and should state so.
  *
  */
case class TxBlob(v: String) extends AnyVal

object TxBlob {
  implicit val codec: Codec[TxBlob] = deriveUnwrappedCodec[TxBlob]

}
