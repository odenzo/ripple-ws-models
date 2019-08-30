package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction

/** Owner ids account , account field is who to deliver it to
  *  No txn specific flags
  */
case class EscrowFinishTx(
    account: AccountAddr,
    owner: AccountAddr,
    offerSequence: TxnSequence,
    condition: Option[String],
    fulfillment: Option[String] // preimage-sha-256 crypto-condition.
) extends RippleTransaction {

  //val txnType: RippleTxnType = RippleTxnType.PaymentTxn

}

object EscrowFinishTx {
  private val tx: (String, Json) = "TransactionType" -> Json.fromString("EscrowFinish")

  // Better to use mapJsonObject and derive encoder?
  implicit val encoder: Encoder.AsObject[EscrowFinishTx] = {
    deriveEncoder[EscrowFinishTx]
      .mapJsonObject(o => tx +: o)
      .mapJsonObject(o => CirceCodecUtils.upcaseFields(o))
  }

  implicit val decoder: Decoder[EscrowFinishTx] = Decoder.instance[EscrowFinishTx] { cursor =>
    for {
      acct    <- cursor.get[AccountAddr]("Account")
      owner   <- cursor.get[AccountAddr]("Owner")
      offseq  <- cursor.get[TxnSequence]("OfferSequence")
      cond    <- cursor.get[Option[String]]("Condition")
      fulfill <- cursor.get[Option[String]]("Fulfillment")
    } yield EscrowFinishTx(acct, owner, offseq, cond, fulfill)

  }
}
