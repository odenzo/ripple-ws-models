package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleScrollingRq, RippleScrollingRs}

/**
  * Get a list of outstanding offers created by an account.
  */
case class AccountOffersRq(account: AccountAddr) extends RippleScrollingRq

case class AccountOffersRs(account: AccountAddr, offers: List[Offer]) extends RippleScrollingRs

object AccountOffersRq extends CirceCodecUtils {

  private type ME = AccountOffersRq
  private val command: String            = "account_offers"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object AccountOffersRs {
  implicit val config: Configuration                  = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[AccountOffersRs] = deriveConfiguredCodec[AccountOffersRs]

}
