package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.syntax._
import io.circe.{Encoder, Decoder, JsonObject}

import com.odenzo.ripple.models.atoms.RippleTxnType.PaymentChannelCreate
import com.odenzo.ripple.models.atoms._

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
    destinationTag: Option[AccountTag],
    sourceTag: Option[AccountTag]
) extends RippleTransaction {}

object PaymentChannelCreateTx {

  implicit val encoder: Encoder.AsObject[PaymentChannelCreateTx] = Encoder.AsObject.instance[PaymentChannelCreateTx] {
    v =>
      JsonObject(
        "TransactionType" := (PaymentChannelCreate: RippleTxnType),
        "Account"         := v.account,
        "Amount"          := v.amount,
        "Destination"     := v.destination,
        "SettleDelay"     := v.settleDelay,
        "PublicKey"       := v.publicKey,
        "CancelAfter"     := v.cancelAfter,
        "DestinationTag"  := v.destinationTag,
        "SourceTag"       := v.sourceTag // FIXME: Duplicated
      )

  }

  implicit val decoder: Decoder[PaymentChannelCreateTx] = Decoder.instance[PaymentChannelCreateTx] { cursor =>
    for {
      acct   <- cursor.get[AccountAddr]("Account")
      amount <- cursor.get[Drops]("Amount")
      dest   <- cursor.get[AccountAddr]("Destination")
      settle <- cursor.get[UInt32]("SettleDelay")
      pubkey <- cursor.get[RipplePublicKey]("PublicKey")
      cancel <- cursor.get[Option[RippleTime]]("CancelAfter")
      dtag   <- cursor.get[Option[AccountTag]]("DestinationTag")
      stag   <- cursor.get[Option[AccountTag]]("SourceTag")
    } yield PaymentChannelCreateTx(acct, amount, dest, settle, pubkey, cancel, dtag, stag)
  }
}
