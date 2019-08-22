package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

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
) extends RippleTransaction {}

object EscrowCancelTx {

  import io.circe.generic.semiauto._

  private final val tx: (String, Json) = "TransactionType" -> Json.fromString("EscrowCancel")

  implicit val derivedEncoder: Encoder.AsObject[EscrowCancelTx] = {
    deriveEncoder[EscrowCancelTx]
      .mapJsonObject(o => tx +: o)
      .mapJsonObject(o => CirceCodecUtils.upcaseFields(o))

  }

  implicit val decoder: Decoder[EscrowCancelTx] = Decoder.instance[EscrowCancelTx] { cursor =>
    for {
      acct     <- cursor.get[AccountAddr]("Account")
      owner    <- cursor.get[AccountAddr]("Owner")
      offerSeq <- cursor.get[TxnSequence]("OfferSequence")
    } yield EscrowCancelTx(acct, owner, offerSeq)

  }

}
