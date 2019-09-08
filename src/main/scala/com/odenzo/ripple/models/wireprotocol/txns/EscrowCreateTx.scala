package com.odenzo.ripple.models.wireprotocol.txns

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}

/**
  *  Create an escrow. No txn specific flagss.
  * @param account
  * @param amount
  * @param destination
  * @param cancelAfter
  * @param finishAfter
  * @param condition
  * @param destinationTag
  * @param sourceTag
  */
case class EscrowCreateTx(
    account: AccountAddr,
    amount: Drops, // This amount is encoded as String in docs!
    destination: AccountAddr,
    cancelAfter: Option[RippleTime]        = None,
    finishAfter: Option[RippleTime]        = None, // as above, need a new type for this.
    condition: Option[String]              = None, // preimage-sha-256 crypto-condition.
    destinationTag: Option[DestinationTag] = None,
    sourceTag: Option[DestinationTag]      = None
) extends RippleTx {

  //val txnType: RippleTxnType = RippleTxnType.PaymentTxn

}

object EscrowCreateTx {
  val name: RippleTxnType                            = RippleTxnType.EscrowCreate
  implicit val config: Configuration                 = configCapitalize.withDefaults
  private val base: Codec.AsObject[EscrowCreateTx]   = deriveConfiguredCodec[EscrowCreateTx]
  implicit val codec: Codec.AsObject[EscrowCreateTx] = wrapTxnCodec(base, name)

}
