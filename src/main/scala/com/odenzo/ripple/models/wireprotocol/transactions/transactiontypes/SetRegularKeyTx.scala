package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._
import io.circe.{Json, Encoder, Decoder}

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * There are no transaction specific flags.
  * @param account    Master account
  * @param regularKey Null to remove, otherwise the regular key address to bind to master account/keys
  */
case class SetRegularKeyTx(account: AccountAddr, regularKey: Option[AccountAddr]) extends RippleTransaction

object SetRegularKeyTx {

  private val txType: RippleTxnType = RippleTxnType.SetRegularKey
  private val tx: (String, Json)    = "TransactionType" -> txType.asJson

  // Better to use mapJsonObject and derive encoder?
  implicit val encoder: Encoder.AsObject[SetRegularKeyTx] = {
    deriveEncoder[SetRegularKeyTx]
      .mapJsonObject(o => tx +: o)
      .mapJsonObject(o => CirceCodecUtils.upcaseFields(o))
  }
  implicit val decoder: Decoder[SetRegularKeyTx] = Decoder.instance[SetRegularKeyTx] { cursor =>
    for {
      acct       <- cursor.get[AccountAddr]("Account")
      regularKey <- cursor.get[Option[AccountAddr]]("RegularKey")
    } yield SetRegularKeyTx(acct, regularKey)

  }

}
