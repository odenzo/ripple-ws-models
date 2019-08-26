package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.atoms.{LedgerSequence, RippleQuality, FiatAmount, TxnHash}
import com.odenzo.ripple.models.support.RippleCodecUtils
import com.odenzo.ripple.models.utils.CirceCodecUtils

// TODO: Need a RippleStateFlag
case class RippleStateNode(
    balance: Option[FiatAmount],
    lowLimit: Option[FiatAmount],
    highLimit: Option[FiatAmount],
    prevTxnId: Option[TxnHash],
    prevTxnLgrSeq: Option[LedgerSequence],
    lowNode: Option[LedgerNodeIndex],
    highNode: Option[LedgerNodeIndex],
    lowQualityIn: Option[RippleQuality], // Should have a RippleQuality type
    lowQualityOut: Option[RippleQuality],
    highQualityIn: Option[RippleQuality],
    highQualityOut: Option[RippleQuality]
) extends LedgerNode

object RippleStateNode {
  implicit val config: Configuration                  = CirceCodecUtils.capitalizeConfiguration
  implicit val codec: Codec.AsObject[RippleStateNode] = deriveConfiguredCodec[RippleStateNode]

}
