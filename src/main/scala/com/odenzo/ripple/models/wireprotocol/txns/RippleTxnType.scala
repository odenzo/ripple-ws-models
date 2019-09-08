package com.odenzo.ripple.models.wireprotocol.txns

import scala.collection.immutable

import enumeratum._

sealed trait RippleTxnType extends EnumEntry

/**
  *
  */
case object RippleTxnType extends Enum[RippleTxnType] with CirceEnum[RippleTxnType] {

  val values: immutable.IndexedSeq[RippleTxnType] = findValues

  case object Payment extends RippleTxnType

  case object OfferCreate extends RippleTxnType

  case object OfferCancel extends RippleTxnType

  case object TrustSet extends RippleTxnType

  case object AccountSet extends RippleTxnType

  case object SetRegularKey extends RippleTxnType

  case object EscrowCreate extends RippleTxnType

  case object EscrowCancel extends RippleTxnType

  case object EscrowFinish extends RippleTxnType

  case object PaymentChannelCreate extends RippleTxnType

  case object PaymentChannelFund extends RippleTxnType

  case object PaymentChannelClaim extends RippleTxnType

  case object SignerListSet extends RippleTxnType

  def asName(c: RippleTxnType) = {
    c.entryName
  }

}
