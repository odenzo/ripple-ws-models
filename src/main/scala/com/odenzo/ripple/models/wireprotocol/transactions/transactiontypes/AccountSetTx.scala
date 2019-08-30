package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction

/**
  * Set the flage Options on a Ripple Account.
  * https://xrpl.org/accountset.html
  * Lots of other things can be set, but I don't worry about them.
  * The flags here are not bit flags.
  */
case class AccountSetTx(
    account: AccountAddr,
    setFlag: Option[AccountSetFlag],
    clearFlag: Option[AccountSetFlag] = None,
    domain: Option[Blob] = None,
    transferRate: Option[Long] = None
) extends RippleTransaction

object AccountSetTx {

  implicit val encoder: Encoder.AsObject[AccountSetTx] = Encoder.AsObject.instance[AccountSetTx] { v =>
    JsonObject(
      "TransactionType" := "AccountSet",
      "Account"         := v.account,
      "SetFlag"         := v.setFlag,
      "ClearFlag"       := v.clearFlag,
      "TransferRate"    := v.transferRate
    )
  }

  implicit val decoder: Decoder[AccountSetTx] =
    Decoder.forProduct5("Account", "SetFlag", "ClearFlag", "Domain", "TransferRate")(AccountSetTx.apply)

}
