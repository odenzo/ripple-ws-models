package com.odenzo.ripple.models.atoms

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

/** Atom that is found in account_lines result   */
case class TrustLine(
    account: AccountAddr,
    balance: BigDecimal,
    currency: Currency,
    limit: BigDecimal,
    limit_peer: BigDecimal,
    quality_in: Long,
    quality_out: Long,
    no_ripple: Option[Boolean] = Some(false),
    no_ripple_peer: Option[Boolean] = Some(false),
    freeze: Option[Boolean] = Some(false),
    freeze_peer: Option[Boolean] = Some(false)
) {

  def toFiatAmount: FiatAmount = FiatAmount(balance, Script(currency, account))
}

object TrustLine {

  implicit val encoder: Encoder.AsObject[TrustLine] = deriveEncoder[TrustLine]
  implicit val decoder: Decoder[TrustLine]          = deriveDecoder[TrustLine]
}
