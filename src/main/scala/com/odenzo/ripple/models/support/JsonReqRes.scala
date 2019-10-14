package com.odenzo.ripple.models.support

import cats.Show
import cats.data.IndexedStateT
import cats.implicits._
import io.circe.generic.semiauto._
import io.circe.{Encoder, Json, JsonObject}
import io.circe.syntax._

/** nb. Is response is Json not JsonObject then its an error and handled as Either. */
case class JsonReqRes(rq: Json, rs: Json)

object JsonReqRes {
  def empty = JsonReqRes(Json.Null, Json.Null)

  implicit val show: Show[JsonReqRes]              = Show.show[JsonReqRes](rr => rr.asJson.spaces4)
  implicit val codec: Encoder.AsObject[JsonReqRes] = deriveCodec[JsonReqRes]

}
