package com.odenzo.ripple.models.atoms

import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec
import io.circe.generic.extras.Configuration

case class FeeDrops(base_fee: Drops, median_fee: Drops, minimum_fee: Drops, open_ledger_fee: Drops)

object FeeDrops {
  implicit val config: Configuration           = Configuration.default
  implicit val codec: Codec.AsObject[FeeDrops] = deriveConfiguredCodec[FeeDrops]
}
case class FeeLevels(median_level: Long, minimum_level: Long, open_ledger_level: Long, reference_level: Long)

object FeeLevels {
  implicit val config: Configuration            = Configuration.default
  implicit val codec: Codec.AsObject[FeeLevels] = deriveConfiguredCodec[FeeLevels]
}
