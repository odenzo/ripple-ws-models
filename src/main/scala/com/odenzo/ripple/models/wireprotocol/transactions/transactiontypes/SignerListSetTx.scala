package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredEncoder, deriveConfiguredDecoder}
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.{Encoder, JsonObject, Decoder}

import com.odenzo.ripple.models.atoms.RippleTxnType.SignerListSet
import com.odenzo.ripple.models.atoms.{AccountAddr, SignerEntry}
import com.odenzo.ripple.models.utils.CirceCodecUtils

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
) extends RippleTransaction

object SignerListSetTx {

  implicit val config: Configuration             = CirceCodecUtils.capitalizeExcept
  implicit val decoder: Decoder[SignerListSetTx] = deriveConfiguredDecoder[SignerListSetTx]
  implicit val encoder: Encoder.AsObject[SignerListSetTx] =
    deriveConfiguredEncoder[SignerListSetTx].mapJsonObject(_.add("TransactionType", "TrustSet".asJson))

}
