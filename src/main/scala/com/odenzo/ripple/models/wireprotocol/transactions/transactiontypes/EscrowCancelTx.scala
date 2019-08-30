package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.generic.extras.Configuration

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction

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
) extends RippleTransaction

object EscrowCancelTx extends CirceCodecUtils {

  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val config: Configuration                   = capitalizeConfig
  implicit val encoder: Codec.AsObject[EscrowCancelTx] = deriveConfiguredCodec[EscrowCancelTx]

}
