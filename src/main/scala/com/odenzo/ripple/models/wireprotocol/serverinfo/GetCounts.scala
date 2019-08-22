package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

/**
  * https://ripple.com/build/rippled-apis/#get-counts
  *
  * @param min_count   Filters fields with counts less than this value
  * @param id
  */
case class GetCountsRq(min_count: Long = 0, id: RippleMsgId = RippleMsgId.random) extends RippleAdminRq

case class GetCountsRs(counts: Json) extends RippleAdminRs

object GetCountsRq {
  val command: (String, Json) = "command" -> "get_counts".asJson
  implicit val encoder: Encoder.AsObject[GetCountsRq] = {
    deriveEncoder[GetCountsRq].mapJsonObject(o => command +: o)
  }
}

object GetCountsRs {

  /**
    * This decoder is used on the result field, but has an arbitrary
    * content. So, actually we just use the result object as Json
    * stored in counts field
    */
  implicit val decoder: Decoder[GetCountsRs] = Decoder.instance[GetCountsRs] { hc =>
    Right(GetCountsRs(hc.value))
  }
}
