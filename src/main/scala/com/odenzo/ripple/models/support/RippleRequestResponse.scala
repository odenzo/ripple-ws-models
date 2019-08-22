package com.odenzo.ripple.models.support

import cats.Show
import cats.implicits._
import io.circe.generic.semiauto._
import io.circe.{Json, Encoder}

import com.odenzo.ripple.models.atoms.{Marker, RippleMsgId, Limit}

/** These are normally objects, but for potential  error cases keep as Json for now */
case class JsonReqRes(rq: Json, rs: Json)

object JsonReqRes {
  def empty = JsonReqRes(Json.Null, Json.Null)

  implicit val show: Show[JsonReqRes] = Show.show[JsonReqRes] { rr =>
    s"""
       | rq: ${rr.rq.show}
       | rs: ${rr.rs.show}
     """.stripMargin

  }

  implicit val encoder: Encoder.AsObject[JsonReqRes] = deriveEncoder[JsonReqRes]
}

/** All Command requests should extend this or AdminInquiryRequest
  *  At least its easy to use Hierarchy tools to find all Requests in IDE
  *  See RippleTransactions for tx_json transaction support.
  */
trait RippleRq extends Serializable with Product {
  val id: RippleMsgId

}

/**
  * This represents the `result` field of the JSON returns (modelled by RippleGenericResponse)
  * RippleAnswer[B<:RippleRs] is typical harness used.
  */
trait RippleRs extends Serializable {}

/** A request that has to be sent over admin channel. So far none of them need scrolling. with ScrollingSupport for
  *  that.
  */
trait RippleAdminRq extends RippleRq

trait RippleAdminRs extends RippleRs

/**
  * Hmm, typically a scrolling request has a limit. Values less then 10 seem to be ignored
  */
trait RippleScrollingRq extends RippleRq {

  //def scrollWith(marker:Option[Json]):RippleScrollingRq

  /** Must have a possible marker, which is just an opaque Json blob */
  def limit: Limit
  def marker: Option[Marker]

  //
  //def scrollWith(marker: Option[Json]) = this.copy(marker = marker)

}

trait RippleScrollingRs extends RippleRs {
  def marker: Option[Marker]
}
