package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.statenodes.EscrowNode
import com.odenzo.ripple.models.utils.CirceCodecUtils

case class TrustSetTx(
    account: AccountAddr,
    limitAmount: FiatAmount,
    flags: Option[BitMask[TrustSetFlag]] = None,
    qualityIn: Option[Long]              = None,
    qualityOut: Option[Long]             = None
) extends RippleTransaction

object TrustSetTx {

  implicit val config: Configuration        = CirceCodecUtils.capitalizeExcept()
  implicit val decoder: Decoder[TrustSetTx] = deriveConfiguredDecoder[TrustSetTx]
  implicit val encoder: Encoder.AsObject[TrustSetTx] =
    deriveConfiguredEncoder[TrustSetTx].mapJsonObject(_.add("TransactionType", "TrustSet".asJson))
}
