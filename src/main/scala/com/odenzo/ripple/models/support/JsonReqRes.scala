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

  implicit val show: Show[JsonReqRes] = Show.show[JsonReqRes] { rr =>
    s"""
       | rq: ${rr.rq.asJson.spaces4}
       | rs: ${rr.rs.asJson.spaces4}
     """.stripMargin

  }

  implicit val encoder: Encoder.AsObject[JsonReqRes] = deriveEncoder[JsonReqRes]
}

// There is also a sealed SubscribeRq and SubscribeRs trait in Subcribe command
