package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * See also docs for account root node. I am guessing this has delta too?
  *
  */
case class PayChannelNode(
    flags: Option[Long],
    account: Option[AccountAddr],
    sequence: Option[TxnSequence],
    takerPays: Option[CurrencyAmount],
    takerGets: Option[CurrencyAmount],
    bookDirectory: Option[AccountAddr],
    bookNode: Option[UInt64], // really an option
    expiration: Option[RippleTime],
    ownerNode: Option[UInt64], // LedgerNodeIndex type.
    previousTxnId: Option[TxnHash],
    previousTxnLgrSeq: Option[LedgerSequence],
    index: Option[String] // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object PayChannelNode {
  implicit val config: Configuration                 = CirceCodecUtils.configCapitalizeExcept()
  implicit val codec: Codec.AsObject[PayChannelNode] = deriveConfiguredCodec[PayChannelNode]

}
