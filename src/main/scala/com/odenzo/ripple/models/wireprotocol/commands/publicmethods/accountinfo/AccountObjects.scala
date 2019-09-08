package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleScrollingRq, RippleScrollingRs}

/** TODO: This is not used yet, so not fully implemented. tipe needs to be mapped (and enumerated)
  * See  https://ripple.com/build/rippled-apis/#account-objects
  * @param tipe eqv to type.  If included, filter results to include only this type of ledger object.
  *             The valid types are: check, deposit_preauth, escrow, offer, payment_channel, signer_list, and state (trust line).
  */
case class AccountObjectsRq(account: AccountAddr, tipe: Option[String])        extends RippleScrollingRq
case class AccountObjectsRs(account: AccountAddr, account_objects: List[Json]) extends RippleScrollingRs

object AccountObjectsRq extends CirceCodecUtils {

  private type ME = AccountObjectsRq
  private val command: String = "account_objects"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object AccountObjectsRs {
  implicit val codec: Codec.AsObject[AccountObjectsRs] = deriveCodec[AccountObjectsRs]
}
