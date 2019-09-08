package com.odenzo.ripple.models.atoms

import io.circe.generic.extras.Configuration
import io.circe.Codec
import io.circe.generic.extras.semiauto._

/** Atom that is found in account_lines result   */
case class TrustLine(
    account: AccountAddr,
    balance: BigDecimal,
    currency: Currency,
    limit: BigDecimal,
    limit_peer: BigDecimal,
    quality_in: RippleQuality,
    quality_out: RippleQuality,
    no_ripple: Boolean = Boolean.box(false), // even withDefault I get a decoding error on this.
    no_ripple_peer: Boolean = Boolean.box(false),
    authorized: Boolean = false,
    peer_authorized: Boolean = false,
    freeze: Boolean = false,
    freeze_peer: Boolean = false
) {

  def toFiatAmount: FiatAmount = FiatAmount(balance, Script(currency, account))
}

object TrustLine {

  implicit val config: Configuration            = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[TrustLine] = deriveConfiguredCodec[TrustLine]
}
