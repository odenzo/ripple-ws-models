package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}

/** Funding an existing payment channel.
  * [[https://ripple.com/build/transactions/#paymentchannelcreate]]
  *
  *  No txn specific flags
  */
case class PaymentChannelCreateTx(
    account: AccountAddr,
    amount: Drops,
    destination: AccountAddr,
    settleDelay: UInt32, // ? Duration in what? seconds I assume.  Ex 1 day in seconds
    publicKey: RipplePublicKey,
    cancelAfter: Option[RippleTime],
    destinationTag: Option[DestinationTag],
    sourceTag: Option[SourceTag]
) extends RippleTx {}

object PaymentChannelCreateTx {
  implicit val config: Configuration                         = configCapitalize.withDefaults
  private val base: Codec.AsObject[PaymentChannelCreateTx]   = deriveConfiguredCodec[PaymentChannelCreateTx]
  implicit val codec: Codec.AsObject[PaymentChannelCreateTx] = wrapTxnCodec(base, RippleTxnType.PaymentChannelCreate)
}
