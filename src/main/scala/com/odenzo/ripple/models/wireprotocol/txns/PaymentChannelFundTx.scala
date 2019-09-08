package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}

/** Funding an existing payment channel.
  * [[https://ripple.com/build/transactions/#paymentchannelfund ]]
  *
  *   This is on ledger and has account, needs seq.
  *   There are no txn specific flags.
  */
case class PaymentChannelFundTx(
    account: AccountAddr,
    channel: PaymentChannelHash,
    amount: Drops = Drops.zero,
    expiration: RippleTime
) extends RippleTx

object PaymentChannelFundTx {
  implicit val config: Configuration                       = configCapitalize.withDefaults
  private val base: Codec.AsObject[PaymentChannelFundTx]   = deriveConfiguredCodec[PaymentChannelFundTx]
  implicit val codec: Codec.AsObject[PaymentChannelFundTx] = wrapTxnCodec(base, RippleTxnType.PaymentChannelFund)
}
