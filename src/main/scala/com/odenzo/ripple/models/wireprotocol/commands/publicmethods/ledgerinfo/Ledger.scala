package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.ledgerinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.LedgerHeader
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq, RippleScrollingRs, RippleScrollingRq}

/**
  * https://ripple.com/build/rippled-apis/#ledger
  * @todo This is pretty basic
  *       TODO: Lots more options here. Will never enable full. Owner funds and expand we need
  *       though
  *
  * Some options options return lots of data and necisitate running on admin socket.
  * This will not be automatically enforced. // TODO: Future, filter match on Json and flag is non-admin socket in
  * comms module -- probably better to put a doesRequireAdmin(json):Boolean function here.
  *
  * [[https://ripple.com/build/rippled-apis/#ledger]]
  *
  * @param transactions
  * @param accounts
  */
case class LedgerRq(
    transactions: Boolean = true,
    accounts: Boolean     = false,
    expand: Boolean       = false,
    owner_funds: Boolean  = true
) extends RippleRq

/**
  *
  * @param ledger    Overview information, embedded may have full transactions or just hashes.
  */
case class LedgerRs(ledger: LedgerHeader) extends RippleRs

object LedgerRq extends CirceCodecUtils {

  private val command: String = "ledger"

  implicit val config: Configuration           = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[LedgerRq] = wrapCommandCodec(command, deriveConfiguredCodec[LedgerRq])
}

object LedgerRs {
  // Haven't built all the encoders for LedgerHeader yet.
  implicit val codec: Codec.AsObject[LedgerRs] = deriveCodec[LedgerRs]
}
