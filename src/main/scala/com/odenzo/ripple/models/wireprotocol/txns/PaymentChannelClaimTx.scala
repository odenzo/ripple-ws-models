package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}

/**
  *  Claim from a Payment Channel
  * [[https://ripple.com/build/transactions/#paymentchannelclaim]]
  *
  *  No account field and does not need a Sequence number. Example doesn't even show TransactionType field!
  *   Haven't actually used it yet.
  *   No transaction specific flags.
  */
case class PaymentChannelClaimTx(
    channel: PaymentChannelHash,
    balance: Option[Drops],
    amount: Option[Drops],
    signature: Option[TxnHash], // FIXME: Incorrect?
    publicKey: Option[RipplePublicKey],
    flags: BitMask[PaymentChannelFlag] = BitMask.empty[PaymentChannelFlag]
) extends RippleTx

object PaymentChannelClaimTx {
  implicit val config: Configuration                        = configCapitalize.withDefaults
  private val base: Codec.AsObject[PaymentChannelClaimTx]   = deriveConfiguredCodec
  implicit val codec: Codec.AsObject[PaymentChannelClaimTx] = wrapTxnCodec(base, RippleTxnType.PaymentChannelClaim)
}
