package com.odenzo.ripple.models.atoms

import io.circe.generic.semiauto.deriveDecoder
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json, JsonObject}

// Put all the type defs in package object?
//type Marker = Option[Json]
/**
  * This is usually an embedded in a Result top level response for requests that
  * do scrolling and Pagination.
  * TODO: In Progress
  * @param ledger_index_max
  * @param ledger_index_min
  * @param limit
  */
case class Pagination(
    limit: Option[Int],
    marker: Option[Json],
    ledger_index_max: LedgerSequence,
    ledger_index_min: LedgerSequence
) {
  def hasMore: Boolean = marker.isDefined
}

object Pagination {

  val defaultPaging =
    new Pagination(limit = Some(50), marker = None, LedgerSequence.WILDCARD_LEDGER, LedgerSequence.WILDCARD_LEDGER)

  // Second attempt at trying to use objectDecoder as this is an embedded element.
  implicit val objEncoder: Encoder.AsObject[Pagination] = Encoder.AsObject.instance[Pagination] { ss =>
    JsonObject
      .singleton("ledger_index_max", ss.ledger_index_max.asJson)
      .add("ledger_index_min", ss.ledger_index_min.asJson)
      .add("limit", ss.limit.asJson)
      .add("marker", ss.marker.asJson)
  }

  // decoder can just be auto generated
  implicit val decoder: Decoder[Pagination] = deriveDecoder[Pagination]

}
