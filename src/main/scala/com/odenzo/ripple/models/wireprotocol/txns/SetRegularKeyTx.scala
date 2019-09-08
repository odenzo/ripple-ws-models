package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}

/**
  * There are no transaction specific flags.
  * @param account    Master account
  * @param regularKey Null to remove, otherwise the regular key address to bind to master account/keys
  */
case class SetRegularKeyTx(account: AccountAddr, regularKey: Option[AccountAddr]) extends RippleTx

object SetRegularKeyTx {

  implicit val config: Configuration                  = configCapitalize.withDefaults
  private val base: Codec.AsObject[SetRegularKeyTx]   = deriveConfiguredCodec[SetRegularKeyTx]
  implicit val codec: Codec.AsObject[SetRegularKeyTx] = wrapTxnCodec(base, RippleTxnType.SetRegularKey)
}
