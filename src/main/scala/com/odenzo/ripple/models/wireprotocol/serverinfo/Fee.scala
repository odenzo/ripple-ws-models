package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

// TODO: Make Fee object and allow to levels and drops
case class FeeRq(id: RippleMsgId = RippleMsgId.random) extends RippleRq

case class FeeRs(
    current_ledger_size: Long,
    current_queue_size: Long,
    drops: FeeDrops,
    ledger_current_index: LedgerSequence,
    expected_ledger_size: Long,
    levels: FeeLevels,
    max_queue_size: Long
) extends RippleRs

object FeeRq {
  val command: (String, Json) = "command" -> "fee".asJson
  implicit val encoder: Encoder.AsObject[FeeRq] = {
    deriveEncoder[FeeRq].mapJsonObject(o => command +: o)
  }
}

object FeeRs {
  implicit val decoder: Decoder[FeeRs] = deriveDecoder[FeeRs]
}

case class FeeDrops(base_fee: Drops, median_fee: Drops, minimum_fee: Drops, open_ledger_fee: Drops)
object FeeDrops {
  implicit val decoder: Decoder[FeeDrops] = deriveDecoder[FeeDrops]
}
case class FeeLevels(median_level: Long, minimum_level: Long, open_ledger_level: Long, reference_level: Long)

object FeeLevels {
  implicit val decoder: Decoder[FeeLevels] = deriveDecoder[FeeLevels]
}
