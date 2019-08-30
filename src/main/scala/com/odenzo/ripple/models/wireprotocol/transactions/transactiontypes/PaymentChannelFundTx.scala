package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.syntax._
import io.circe.{Encoder, JsonObject, Decoder}

import com.odenzo.ripple.models.atoms.RippleTxnType.PaymentChannelFund
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction

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
) extends RippleTransaction

object PaymentChannelFundTx {

  val txnType: RippleTxnType = PaymentChannelFund

  implicit val encoder: Encoder.AsObject[PaymentChannelFundTx] = Encoder.AsObject.instance[PaymentChannelFundTx] { v =>
    JsonObject(
      "TransactionType" := txnType,
      "Channel"         := v.channel,
      "Amount"          := v.amount,
      "Expiration"      := v.expiration
    )

  }

  implicit val decoder: Decoder[PaymentChannelFundTx] = Decoder.instance[PaymentChannelFundTx] { cursor =>
    for {
      acct     <- cursor.get[AccountAddr]("Account")
      channels <- cursor.get[PaymentChannelHash]("Channel")
      amount   <- cursor.get[Drops]("Amount")
      expires  <- cursor.get[RippleTime]("Expiration")
    } yield PaymentChannelFundTx(acct, channels, amount, expires)
  }
}
