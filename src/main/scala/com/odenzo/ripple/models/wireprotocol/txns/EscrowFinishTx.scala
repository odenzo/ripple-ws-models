package com.odenzo.ripple.models.wireprotocol.txns

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}

/** Owner ids account , account field is who to deliver it to
  *  No txn specific flags
  */
case class EscrowFinishTx(
    account: AccountAddr,
    owner: AccountAddr,
    offerSequence: TxnSequence,
    condition: Option[String],
    fulfillment: Option[String] // preimage-sha-256 crypto-condition.
) extends RippleTx

object EscrowFinishTx {
  implicit val config: Configuration                 = configCapitalize.withDefaults
  private val base: Codec.AsObject[EscrowFinishTx]   = deriveConfiguredCodec[EscrowFinishTx]
  implicit val codec: Codec.AsObject[EscrowFinishTx] = wrapTxnCodec(base, RippleTxnType.EscrowFinish)
}
