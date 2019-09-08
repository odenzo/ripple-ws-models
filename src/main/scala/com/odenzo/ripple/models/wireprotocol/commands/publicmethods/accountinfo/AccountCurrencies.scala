package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import monocle.macros.Lenses

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}

@Lenses("_") case class AccountCurrenciesRq(
    account: AccountAddr,
    strict: Boolean = true
) extends RippleRq {}

case class AccountCurrenciesRs(
    receive_currencies: List[Currency],
    send_currencies: List[Currency]
) extends RippleRs

object AccountCurrenciesRq extends CirceCodecUtils {

  private type ME = AccountCurrenciesRq
  private val command: String = "account_currencies"

  val defaultResponse: AccountCurrenciesRs.type = AccountCurrenciesRs

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object AccountCurrenciesRs {

  import io.circe._
  import io.circe.generic.extras.semiauto._
  implicit val config: Configuration                      = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[AccountCurrenciesRs] = deriveConfiguredCodec[AccountCurrenciesRs]

}
