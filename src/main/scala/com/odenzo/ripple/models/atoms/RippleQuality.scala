package com.odenzo.ripple.models.atoms

import io.circe.{Decoder, Encoder}
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

/**
  * The conversion rate from two FiatAmounts of the same currency when they "Ripple"
  * as an integer in the implied ratio 1,000,000,000. The value 0 is equivalent to 1 billion, or face
  * value.
  *
  * @param v See
  */
case class RippleQuality(v: UInt32 = UInt32.ZERO) {}

object RippleQuality {

  implicit val coodec: Codec[RippleQuality] = deriveUnwrappedCodec[RippleQuality]

}
