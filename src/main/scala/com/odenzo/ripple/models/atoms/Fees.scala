package com.odenzo.ripple.models.atoms

import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec
import io.circe.generic.extras.Configuration

/** The fees in the last ledger from FeeRq normally. */
case class FeeInfo(base_fee: Drops, median_fee: Drops, minimum_fee: Drops, open_ledger_fee: Drops)

object FeeInfo {
  implicit val config: Configuration          = Configuration.default
  implicit val codec: Codec.AsObject[FeeInfo] = deriveConfiguredCodec[FeeInfo]
}

/** FeeLevel from FeeRq, specified in Fee Levels https://xrpl.org/transaction-cost.html#fee-levels */
case class FeeLevels(medianLevel: Long, minimumLevel: Long, openLedgerLevel: Long, referenceLevel: Long)

object FeeLevels {
  implicit val config: Configuration            = Configuration.default.withSnakeCaseMemberNames
  implicit val codec: Codec.AsObject[FeeLevels] = deriveConfiguredCodec[FeeLevels]
}
