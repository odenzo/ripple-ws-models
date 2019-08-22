package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.syntax._
import io.circe.{Encoder, Decoder, JsonObject}

import com.odenzo.ripple.models.atoms.RippleTxnType.PaymentChannelClaim
import com.odenzo.ripple.models.atoms.{Drops, RipplePublicKey, PaymentChannelHash, TxnHash}

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
    publicKey: Option[RipplePublicKey]
    // FIXME: Two new flags to add.
) extends RippleTransaction {}

object PaymentChannelClaimTx {

  implicit val encoder: Encoder.AsObject[PaymentChannelClaimTx] = Encoder.AsObject.instance[PaymentChannelClaimTx] {
    v =>
      JsonObject(
        "TransactionType" := PaymentChannelClaim.entryName,
        "Channel"         := v.channel,
        "Balance"         := v.balance,
        "Amount"          := v.amount,
        "Signature"       := v.signature,
        "PublicKey"       := v.publicKey
      )

  }

  implicit val decoder: Decoder[PaymentChannelClaimTx] = Decoder.instance[PaymentChannelClaimTx] { cursor =>
    for {
      channel <- cursor.get[PaymentChannelHash]("Channel")
      balance <- cursor.get[Option[Drops]]("Balance")
      amount  <- cursor.get[Option[Drops]]("Amount")
      sig     <- cursor.get[Option[TxnHash]]("Signature")
      pubkey  <- cursor.get[Option[RipplePublicKey]]("PublicKey")
    } yield PaymentChannelClaimTx(channel, balance, amount, sig, pubkey)
  }
}
