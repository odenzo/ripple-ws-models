package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.syntax._
import io.circe.{Decoder, HCursor, _}

import com.odenzo.ripple.models.atoms._

case class TrustSetTx(
    account: AccountAddr,
    limitAmount: FiatAmount,
    flags: Option[BitMask[TrustSetFlag]] = None,
    qualIn: Option[Long] = None,
    qualOut: Option[Long] = None
) extends RippleTransaction

object TrustSetTx {

  implicit val encode: Encoder.AsObject[TrustSetTx] = Encoder.AsObject.instance[TrustSetTx] { v =>
    JsonObject(
      "TransactionType" := "TrustSet",
      "Account"         := v.account,
      "LimitAmount"     := v.limitAmount,
      "Flags"           := v.flags,
      "QualityIn"       := v.qualIn,
      "QualityOut"      := v.qualOut
    )

  }

  implicit val decodeTxTrustSet: Decoder[TrustSetTx] = new Decoder[TrustSetTx] {
    final def apply(cursor: HCursor): Decoder.Result[TrustSetTx] = {
      for {
        acct    <- cursor.get[AccountAddr]("Account")
        amount  <- cursor.get[FiatAmount]("LimitAmount")
        flag    <- cursor.get[Option[BitMask[TrustSetFlag]]]("Flags")
        qualIn  <- cursor.get[Option[Long]]("QualityIn")
        qualOut <- cursor.get[Option[Long]]("QualityOut")
      } yield TrustSetTx(acct, amount, flag, qualIn, qualOut)
    }
  }
}
