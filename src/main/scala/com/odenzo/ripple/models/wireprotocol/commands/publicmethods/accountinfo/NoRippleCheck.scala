package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}

/**
  * https://ripple.com/build/rippled-apis/#noripple-check
  * TODO: Not using this yet, but basic round-trip should work.
  * You cannot use "CURRENT" ledger though, because (a) its crazy and it returns ledger_current_index instead of
  * ledger_index
  *
  * @param account   Account Address to check
  * @param role user or gateway
  * @param transactions Generate transaction information to fix any problems (delta from recommendations)

  */
case class NoRippleCheckRq(account: AccountAddr, role: String = "user", transactions: Boolean = false) extends RippleRq

/**
  *  Oh, ledger_current_index returned if ask for "current". If ask for validated then get
  *  the standard ldeger_hash and ledger_index. Since I can't think of any reason not to do this on
  *  a valid or old historical ledger, we ignore the ledger_current_index.
  *  This is probably a pattern everywhere in Ripple... "problem" in quotes, because current is not stable?
  *  * @param ledger_index
  * @param problems
  * @param transactions
  */
case class NoRippleCheckRs(problems: List[String], transactions: List[Json]) extends RippleRs

object NoRippleCheckRq extends CirceCodecUtils {

  private type ME = NoRippleCheckRq
  private val command: String            = "noripple_check"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object NoRippleCheckRs {
  implicit val codec: Codec.AsObject[NoRippleCheckRs] = deriveCodec[NoRippleCheckRs]
}
