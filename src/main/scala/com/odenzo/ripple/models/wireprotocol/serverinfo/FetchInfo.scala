package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

/**
  * https://ripple.com/build/rippled-apis/#fetch-info
  * This is another admin command used for diagnosing RCL network really.
  * @param id
  */
case class FetchInfoRq(id: RippleMsgId = RippleMsgId.random) extends RippleAdminRq

case class FetchInfoRs(info: Json) extends RippleAdminRs

object FetchInfoRq {
  val command: (String, Json) = "command" -> "fetch_info".asJson
  implicit val encoder: Encoder.AsObject[FetchInfoRq] = {
    deriveEncoder[FetchInfoRq].mapJsonObject(o => command +: o)
  }
}

object FetchInfoRs {
  implicit val decoder: Decoder[FetchInfoRs] = deriveDecoder[FetchInfoRs]
}
