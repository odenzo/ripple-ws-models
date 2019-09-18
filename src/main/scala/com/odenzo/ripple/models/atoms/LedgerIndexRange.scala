package com.odenzo.ripple.models.atoms

import scala.collection.immutable.NumericRange
import scala.util.Try

import io.circe.{Encoder, Decoder}

import com.odenzo.ripple.models.utils.caterrors.ModelsLibError

case class LedgerIndexRange(start: LedgerSequence, end: LedgerSequence) {
  def asRange: NumericRange.Inclusive[Long] = start.v to end.v
}

object LedgerIndexRange {

  implicit val encoder: Encoder[LedgerIndexRange] =
    Encoder.encodeString.contramap[LedgerIndexRange](v => s"${v.start.v}-${v.end.v}")

  implicit val decoder: Decoder[LedgerIndexRange] = Decoder.decodeString.emapTry { s: String =>
    Try {
      s.split('-').toList match {
        case start :: end :: Nil =>
          LedgerIndexRange(
            LedgerSequence(java.lang.Long.parseLong(start)),
            LedgerSequence(java.lang.Long.parseLong(end))
          )

        case invalid => throw ModelsLibError(s"$invalid was not in [start]-[end] format.")
      }
    }
  }

}
