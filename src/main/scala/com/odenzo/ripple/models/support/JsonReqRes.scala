package com.odenzo.ripple.models.support

import cats.Show
import cats.data.IndexedStateT
import cats.implicits._
import io.circe.generic.semiauto._
import io.circe.{Encoder, JsonObject}
import io.circe.syntax._

/** nb. Is response is Json not JsonObject then its an error and handled as Either. */
case class JsonReqRes(rq: JsonObject, rs: JsonObject)

object JsonReqRes {
  def empty = JsonReqRes(JsonObject.empty, JsonObject.empty)

  implicit val show: Show[JsonReqRes]              = Show.show[JsonReqRes](rr => rr.asJson.spaces4)
  implicit val codec: Encoder.AsObject[JsonReqRes] = deriveCodec[JsonReqRes]

}
