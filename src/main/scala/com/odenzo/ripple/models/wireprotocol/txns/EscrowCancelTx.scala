package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * Don't understand the correct use case for this.
  * I create an Escrow (account to account) with cancel time, then try and cancel it before cancel time.
  * Get no persmission. Wait until after cancel time expires and....
  *  No txn specific flags
  * @param account
  * @param owner
  * @param offerSequence

  */
case class EscrowCancelTx(
    account: AccountAddr, // Not neded?
    owner: AccountAddr,
    offerSequence: TxnSequence // This is the sequence field in EscrowCreate
) extends RippleTx

object EscrowCancelTx extends CirceCodecUtils {
  implicit val config: Configuration                 = configCapitalize
  private val base: Codec.AsObject[EscrowCancelTx]   = deriveConfiguredCodec[EscrowCancelTx]
  implicit val codec: Codec.AsObject[EscrowCancelTx] = wrapTxnCodec(base, RippleTxnType.EscrowCancel)
}
