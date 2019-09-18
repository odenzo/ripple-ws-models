package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.generic.extras.Configuration
import io.circe._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.atoms.{RippleQuality, FiatAmount, TxnHash, LedgerSequence, LedgerHash}
import com.odenzo.ripple.models.utils.CirceCodecUtils

// TODO: Need a RippleStateFlag
/**
  *   This is an important ledger node that can be used to check the trustline between two accounts.
  *  It aggregates both sides of the trust line
  *  .
  * @param balance
  * @param lowLimit
  * @param highLimit
  * @param prevTxnId
  * @param prevTxnLgrSeq
  * @param lowNode
  * @param highNode
  * @param lowQualityIn
  * @param lowQualityOut
  * @param highQualityIn
  * @param highQualityOut
  */
case class RippleStateNode(
    balance: Option[FiatAmount],
    flags: Long,
    highLimit: Option[FiatAmount],
    highNode: Option[LedgerNodeIndex],     // 16 chars 0000 mostly?!
    prevTxnId: Option[TxnHash],            // Optional only on Ledge 1
    prevTxnLgrSeq: Option[LedgerSequence], // Optional only on ledger 1
    lowLimit: Option[FiatAmount],
    lowNode: Option[LedgerNodeIndex],
    lowQualityIn: Option[RippleQuality],   // Should have a RippleQuality type
    lowQualityOut: Option[RippleQuality],  // Really are optional
    highQualityIn: Option[RippleQuality],  // Really are optional
    highQualityOut: Option[RippleQuality], // Really are optionsl
    index: LedgerHash
) extends LedgerNode

object RippleStateNode {
  implicit val config: Configuration                  = CirceCodecUtils.configCapitalizeExcept(Set("index"))
  implicit val codec: Codec.AsObject[RippleStateNode] = deriveConfiguredCodec[RippleStateNode]

}
