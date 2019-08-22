package com.odenzo.ripple.models.atoms

import io.circe.Json

import io.circe._

/**
  *  Limit for paging through results.
  * @param max Usually has to be in range 10 to 400, not clamped now but probably should.
  */
case class Limit(max: Int) {}

object Limit {

  val default = Limit(50)

  implicit val encode: Encoder[Limit]  = Encoder.encodeInt.contramap[Limit](_.max)
  implicit val decoder: Decoder[Limit] = Decoder.decodeInt.map(Limit(_))
}

/** Scrolling requests and responses have an Optional opaque marker to track where scrolling position is.s */
case class Marker(mark: Json)

object Marker {

  implicit val encode: Encoder[Marker]  = Encoder.encodeJson.contramap[Marker](_.mark)
  implicit val decoder: Decoder[Marker] = Decoder.decodeJson.map(Marker(_))
}
