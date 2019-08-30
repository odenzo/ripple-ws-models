package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.syntax._
import io.circe.{Json, Encoder, Decoder}

import com.odenzo.ripple.models.atoms.{TxnSequence, OfferCreateFlag, RippleTime, BitMask, AccountAddr, CurrencyAmount}
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction

/**
  * Creates (or modifies) existing book order offer.
  * https://ripple.com/build/transactions/#offercreate
  *
  * @param expiration
  * @param offerSequence Sequence number of offer to delete before creating this one.
  * @param takerGets
  * @param takerPays
  * @param flags  Read the docs, you should set this manually as important functionality changes
  */
case class OfferCreateTx(
    account: AccountAddr,
    expiration: Option[RippleTime] = None,
    offerSequence: Option[TxnSequence],
    takerGets: CurrencyAmount,
    takerPays: CurrencyAmount,
    flags: BitMask[OfferCreateFlag] = BitMask.empty[OfferCreateFlag] // ACtually should think about this each time
) extends RippleTransaction

object OfferCreateTx {

  import io.circe.generic.semiauto._
  private val command: (String, Json) = "TransactionType" -> "OfferCreate".asJson
  implicit val encoder: Encoder.AsObject[OfferCreateTx] = deriveEncoder[OfferCreateTx]
    .mapJsonObject(o => command +: o)
    .mapJsonObject(o => CirceCodecUtils.upcaseFields(o))

  implicit val decoder: Decoder[OfferCreateTx] = Decoder.instance[OfferCreateTx] { cursor =>
    for {
      acct    <- cursor.get[AccountAddr]("Account")
      expires <- cursor.get[Option[RippleTime]]("Expiration")
      offer   <- cursor.get[Option[TxnSequence]]("OfferSequence") // Really optional?
      gets    <- cursor.get[CurrencyAmount]("TakerGets")
      pays    <- cursor.get[CurrencyAmount]("TakerPays")
      flags   <- cursor.get[BitMask[OfferCreateFlag]]("Flags")

    } yield OfferCreateTx(acct, expires, offer, gets, pays, flags)
  }

}
