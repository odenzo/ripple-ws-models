package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe.Decoder

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.atoms.{FiatAmount, LedgerSequence, RippleQuality, TxnHash}

// TODO: Need a RippleStateFlag
// Does this appear in delta or not?  I think so.
// In Progress
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
  // Quick hack
  // Yet again there must be some way to do this!
  // In this particular case COnfigure a fieldMapper maybe
//  private val fields = "Balance" ::
//               "LowLimit" ::
//               "HighLimit" ::
//               "PreviousTxnID" ::
//               "PreviousTxnLgrSeq" ::
//               "LowNode" ::
//               "HighNode" ::
//               "LowQualityIn" ::
//               "LowQualityOut" ::
//               "HighQualityIn" ::
//               "HighQualityOut" ::
//               HNil
//  private val tupledFields: (String, String, String, String, String, String, String, String, String, String, String) =
//    fields.tupled

  // val listFields: immutable.Seq[String] = fields.toList

  implicit val decoder: Decoder[RippleStateNode] = Decoder.forProduct11(
    "Balance",
    "LowLimit",
    "HighLimit",
    "PreviousTxnID",
    "PreviousTxnLgrSeq",
    "LowNode",
    "HighNode",
    "LowQualityIn",
    "LowQualityOut",
    "HighQualityIn",
    "HighQualityOut"
  )(RippleStateNode.apply)

}
