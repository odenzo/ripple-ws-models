package com.odenzo.ripple.models.atoms

import io.circe.generic.semiauto.deriveDecoder
import io.circe.syntax._
import io.circe.{Json, Encoder, JsonObject, Decoder}
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
// Put all the type defs in package object?
//type Marker = Option[Json]
/**
  * This is usually an embedded in a Result top level response for requests that
  * do scrolling and Pagination.
  * @param ledger_index_max
  * @param ledger_index_min
  * @param limit
  */
case class Pagination(
    limit: Option[Limit],
    marker: Option[Marker],
    ledger_index_max: LedgerSequence,
    ledger_index_min: LedgerSequence
) {
  def hasMore: Boolean = marker.isDefined
}

object Pagination {

  val defaultPaging =
    new Pagination(
      limit  = Some(Limit(50)),
      marker = None,
      LedgerSequence.WILDCARD_LEDGER,
      LedgerSequence.WILDCARD_LEDGER
    )

  implicit val config: Configuration             = Configuration.default
  implicit val codec: Codec.AsObject[Pagination] = deriveConfiguredCodec[Pagination]
}
