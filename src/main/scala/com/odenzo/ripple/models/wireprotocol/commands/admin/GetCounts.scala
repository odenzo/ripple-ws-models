package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}
import com.odenzo.ripple.models.wireprotocol.commands.admin.LedgerRequestRq.wrapCommandCodec

/**
  * https://ripple.com/build/rippled-apis/#get-counts
  *
  * @param min_count   Filters fields with counts less than this value
  */
case class GetCountsRq(min_count: Long = 0) extends RippleAdminRq

case class GetCountsRs(counts: Json) extends RippleAdminRs

object GetCountsRq {
  private type ME = GetCountsRq
  private val command: String = "get_counts"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
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
