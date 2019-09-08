package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.generic.extras.Configuration

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * Set the flage Options on a Ripple Account.
  * https://xrpl.org/accountset.html
  * Lots of other things can be set, but I don't worry about them.
  * The flags here are not bit flags.
  */
case class AccountSetTx(
    account: AccountAddr,
    setFlag: Option[AccountSetFlag],
    clearFlag: Option[AccountSetFlag] = None,
    domain: Option[Blob]              = None,
    transferRate: Option[Long]        = None
) extends RippleTx

object AccountSetTx extends CirceCodecUtils {

  import io.circe._
  import io.circe.generic.extras.semiauto._
  implicit val config: Configuration               = configCapitalize
  private val base: Codec.AsObject[AccountSetTx]   = deriveConfiguredCodec[AccountSetTx]
  implicit val codec: Codec.AsObject[AccountSetTx] = wrapTxnCodec(base, RippleTxnType.AccountSet)

}
