package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.{Encoder, Decoder, JsonObject}

import com.odenzo.ripple.models.atoms.RippleTxnType.SignerListSet
import com.odenzo.ripple.models.atoms.{AccountAddr, SignerEntry}

/** For setting or removing Multi-Signature List.
  *
  * [[https://ripple.com/build/transactions/#signerlistset]]
  * There are no specific flags for this transaction.
  * @param account
  * @param signerQuorum   Target number of Signer weights, 0 to delete a list.
  * @param signerEntries  Entries, omitted when deleting. 1-8 and weights greater than signerQuorum
  */
case class SignerListSetTx(
    account: AccountAddr,
    signerQuorum: Int,
    signerEntries: Option[List[SignerEntry]]
) extends RippleTransaction {}

object SignerListSetTx {
  private case class DummyWrapper(SignerEntry: SignerEntry)
  private object DummyWrapper {
    implicit val decoder: Decoder[DummyWrapper]          = deriveDecoder[DummyWrapper]
    implicit val encoder: Encoder.AsObject[DummyWrapper] = deriveEncoder[DummyWrapper]
  }

  implicit val encoder: Encoder.AsObject[SignerListSetTx] = Encoder.AsObject.instance[SignerListSetTx] { v =>
    JsonObject(
      "TransactionType" := SignerListSet.entryName,
      "Account"         := v.account,
      "SignerQuorum"    := v.signerQuorum,
      "SignerEntries"   := v.signerEntries
    )

  }

  implicit val decoder: Decoder[SignerListSetTx] = Decoder.instance[SignerListSetTx] { cursor =>
    for {
      acct    <- cursor.get[AccountAddr]("Account")
      quorum  <- cursor.get[Int]("SignerQuorum")
      entries <- cursor.get[Option[List[SignerEntry]]]("SignerEntries")
    } yield SignerListSetTx(acct, quorum, entries)

  }

}
