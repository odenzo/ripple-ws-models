package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo

import io.circe.generic.extras.Configuration
import monocle.macros.Lenses
import cats._
import cats.data._
import cats.implicits._
import io.circe._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.statenodes.AccountData
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}

/**
  * https://ripple.com/build/rippled-apis/#account-info
  *
  * @param account  Set to strict so only public key or Account Address, we only allow addr.
  * @param queue    Can only be true when querying the current ledger -- not enforced
  * @param signer_lists Return the signers list info for the account
  * @param strict
  */
@Lenses("_") case class AccountInfoRq(
    account: Account,
    queue: Boolean        = false,
    signer_lists: Boolean = false,
    strict: Boolean       = true
) extends RippleRq

@Lenses("_") case class AccountInfoRs(
    account_data: AccountData,          // Account Root Ledger Object
    signer_lists: Option[List[Signer]], // Really SignerList ledger object SignerListNode
    queue_data: Option[JsonObject]
) extends RippleRs

object AccountInfoRq extends CirceCodecUtils {

  private type ME = AccountInfoRq
  private val command: String = "account_info"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object AccountInfoRs {

  implicit val config: Configuration                = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[AccountInfoRs] = deriveConfiguredCodec[AccountInfoRs]
}
