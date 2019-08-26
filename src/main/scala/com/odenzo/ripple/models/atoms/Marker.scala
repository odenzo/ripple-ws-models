package com.odenzo.ripple.models.atoms

import io.circe.generic.extras.Configuration
import io.circe.{Json, _}
import io.circe.generic.extras.semiauto._

/**
  *  Limit for paging through results.
  * @param max Usually has to be in range 10 to 400, not clamped now but probably should.
  */
case class Limit(max: Int) {}

object Limit {

  val default                        = Limit(50)
  implicit val config: Configuration = Configuration.default
  implicit val codec: Codec[Limit]   = deriveUnwrappedCodec[Limit]
}

/** Scrolling requests and responses have an Optional opaque marker to track where scrolling position is.s */
case class Marker(mark: Json)

object Marker {
  // Is this unwrapped, I think so.
  implicit val config: Configuration = Configuration.default
  implicit val codec: Codec[Marker]  = deriveUnwrappedCodec[Marker]
}
