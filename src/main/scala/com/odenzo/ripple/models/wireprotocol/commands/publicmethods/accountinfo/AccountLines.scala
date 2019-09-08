package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto._
import monocle.macros.Lenses

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleScrollingRq, RippleScrollingRs}

/**
  *  Used to get the trust lines and their balances. For currencies other than XRP.
  *  For XRP balance see AccountInfo
  * @param account
  * @param peer      Limit lines to those between account the this account address
  */
@Lenses("_") case class AccountLinesRq(account: Account, peer: Option[AccountAddr] = None) extends RippleScrollingRq

/** The result field portion for account_lines command response.
  * Add the optional ledger_index , ledger_current_index, ledger_hash */
case class AccountLinesRs(account: AccountAddr, lines: List[TrustLine]) extends RippleScrollingRs

object AccountLinesRq extends CirceCodecUtils {

  private type ME = AccountLinesRq
  private val command: String = "account_lines"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object AccountLinesRs {
  implicit val codec: Codec.AsObject[AccountLinesRs] = deriveCodec[AccountLinesRs]
}
