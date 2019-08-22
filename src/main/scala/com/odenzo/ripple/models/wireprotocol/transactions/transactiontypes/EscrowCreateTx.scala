package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

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
    cancelAfter: Option[RippleTime] = None,
    finishAfter: Option[RippleTime] = None, // as above, need a new type for this.
    condition: Option[String] = None,       // preimage-sha-256 crypto-condition.
    destinationTag: Option[AccountTag] = None,
    sourceTag: Option[AccountTag] = None
) extends RippleTransaction {

  //val txnType: RippleTxnType = RippleTxnType.PaymentTxn

}

object EscrowCreateTx {
  private val tx: (String, Json) = "TransactionType" -> Json.fromString("EscrowCreate")

  import io.circe.generic.semiauto._
  // Better to use mapJsonObject and derive encoder?
  implicit val derivedEncoder: Encoder.AsObject[EscrowCreateTx] = {
    deriveEncoder[EscrowCreateTx]
      .mapJsonObject(o => tx +: o)
      .mapJsonObject(o => CirceCodecUtils.upcaseFields(o))

  }

  implicit val decoder: Decoder[EscrowCreateTx] = Decoder.instance[EscrowCreateTx] { cursor =>
    for {
      acct   <- cursor.get[AccountAddr]("Account")
      owner  <- cursor.get[Drops]("Amount")
      dest   <- cursor.get[AccountAddr]("Destination")
      cancel <- cursor.get[Option[RippleTime]]("CancelAfter")
      finish <- cursor.get[Option[RippleTime]]("finishAfer")
      cond   <- cursor.get[Option[String]]("Condition")
      dt     <- cursor.get[Option[AccountTag]]("DestinationTag")
      st     <- cursor.get[Option[AccountTag]]("SourceTag")
    } yield EscrowCreateTx(acct, owner, dest, cancel, finish, cond, dt, st)

  }

}
