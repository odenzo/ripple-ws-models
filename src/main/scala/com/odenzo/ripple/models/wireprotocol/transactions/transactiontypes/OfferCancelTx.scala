package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.{Json, Encoder, Decoder}

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction

/**
  * Cancels an existing book order offer.
  * [[https://ripple.com/build/transactions/#offercancel]]
  *    No txn specific flags.
  */
case class OfferCancelTx(account: AccountAddr, offerSequence: TxnSequence) extends RippleTransaction

object OfferCancelTx extends CirceCodecUtils {
  private val command: (String, Json) = "TransactionType" -> "OfferCancel".asJson
  implicit val encoder: Encoder.AsObject[OfferCancelTx] = deriveEncoder[OfferCancelTx]
    .mapJsonObject(o => command +: o)
    .mapJsonObject(o => CirceCodecUtils.upcaseFields(o))

  implicit val decoder: Decoder[OfferCancelTx] = Decoder.instance[OfferCancelTx] { cursor =>
    for {
      acct  <- cursor.get[AccountAddr]("Account")
      offer <- cursor.get[TxnSequence]("OfferSequence")
    } yield OfferCancelTx(acct, offer)
  }

}
