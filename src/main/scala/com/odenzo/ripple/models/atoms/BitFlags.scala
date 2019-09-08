package com.odenzo.ripple.models.atoms

import scala.collection.immutable

import enumeratum.values._
import io.circe.{Json, Encoder, Decoder}
import spire.math.{ULong, UInt, SafeLong}

/*
  FIXME: Latest changed to Int make encoding of a flag with global flag set spit out a negative number.

 * This whole thing is a bit tortured.
 * Basic requirements: Enumerated Labels for BitFlags
 * A BitMask to represent a set of typed BitFlags
 * Ability for Circe to encode and decode BitMask from JSON long
 * Ability to do operations on immutable BitMak
 * Ability to go from a bit state to the enumeration
 *
 * My "clever" design is stuck on two things:
 * + Decoders/Encoders are invariant in Circe so have to elaborate
 * + Can't figure out how to go from List[A<:BitFlag] (where A goes to sealed abstract class)
 */

/**  Represents a 32bit flag to set individual bits */
sealed trait BitFlag {
  def value: Int
  def toUInt: UInt = UInt(value)
}

sealed trait BitMaskFlagEnum[A <: BitFlag] {
  this: { val values: IndexedSeq[A] } =>

  def asFlags(mask: BitMask[A]): IndexedSeq[A] = values.filter(mask.isSet)

  def extraBits(mask: BitMask[A]): UInt = {
    val allBits: UInt = values.map(_.toUInt).fold(UInt.MinValue)(_ | _)
    mask.v ^ allBits
  }
}

/**
  * This is a bit tortured. It can represent any flags, including ones not in A.
  * This is to handle global flags, and enable generic parsing.
  * But, you can only do operations with Flags that are of type A
  * Could all B < A.type parameter but noisy
  */
case class BitMask[A <: BitFlag](v: UInt) {

  def replace(v: A): BitMask[A] = this.copy(v = UInt(v.value))
  def clear(): BitMask[A]       = this.copy(UInt.MinValue)

  def or(b: A): BitMask[A]  = this.copy(v = v | UInt(b.value))
  def xor(b: A): BitMask[A] = this.copy(v = v ^ UInt(b.value))
  def and(b: A): BitMask[A] = this.copy(v = v & UInt(b.value))

  def or(mask: BitMask[A]): BitMask[A]  = this.copy(v = mask.v | this.v)
  def xor(mask: BitMask[A]): BitMask[A] = this.copy(v = mask.v ^ this.v)
  def and(mask: BitMask[A]): BitMask[A] = this.copy(v = mask.v & this.v)

  def isSet(b: A) = (v & b.toUInt) =!= UInt.MinValue
}

object BitMask {

  def empty[T <: BitFlag]: BitMask[T] = BitMask[T](UInt.MinValue)

  // This is confusing me. OfferCreateFlag is a type, but each enum has a type also.
  // So, with one flag it has the enum type. Not sure how I can constrain
  def from[T <: BitFlag](flags: T*): BitMask[T] = {
    val ord: UInt = flags.map(bf => bf.toUInt).fold(UInt.MinValue)(_ | _)
    BitMask[T](ord)
  }

  implicit val uIntEncoder: Encoder[UInt] = Encoder[Int].contramap(_.signed)

  implicit val uLongEncoder: Encoder[ULong] = Encoder[Long].contramap(_.signed)

  val bigint = Encoder[BigInt]

  implicit val safeLongEncoder: Encoder[SafeLong] = new Encoder[SafeLong] {
    def apply(a: SafeLong): Json = Json.fromBigInt(a.toBigInt)
  }
  implicit val uIntDecoder: Decoder[UInt]         = Decoder[Long].map(i => UInt(i))
  implicit val uLongDecoder: Decoder[ULong]       = Decoder[Long].map(l => new ULong(l))
  implicit val safeLongDecoder: Decoder[SafeLong] = Decoder[BigInt].map(SafeLong.apply)

  implicit val encode0: Encoder[BitMask[BitFlag]]            = uIntEncoder.contramap(v => (v.v))
  implicit val encode1: Encoder[BitMask[TrustSetFlag]]       = uIntEncoder.contramap(v => (v.v))
  implicit val encode2: Encoder[BitMask[OfferCreateFlag]]    = uIntEncoder.contramap(v => (v.v))
  implicit val encode3: Encoder[BitMask[PaymentFlag]]        = uIntEncoder.contramap(v => (v.v))
  implicit val encode4: Encoder[BitMask[LedgerFlag]]         = uIntEncoder.contramap(v => (v.v))
  implicit val encode5: Encoder[BitMask[AccountRootFlag]]    = uIntEncoder.contramap(v => (v.v))
  implicit val encode6: Encoder[BitMask[PaymentChannelFlag]] = uIntEncoder.contramap(v => (v.v))

  implicit val decode0: Decoder[BitMask[BitFlag]]            = uIntDecoder.map(l => BitMask[BitFlag](l))
  implicit val decode1: Decoder[BitMask[TrustSetFlag]]       = uIntDecoder.map(l => BitMask[TrustSetFlag](l))
  implicit val decode2: Decoder[BitMask[OfferCreateFlag]]    = uIntDecoder.map(l => BitMask[OfferCreateFlag](l))
  implicit val decode3: Decoder[BitMask[PaymentFlag]]        = uIntDecoder.map(l => BitMask[PaymentFlag](l))
  implicit val decode4: Decoder[BitMask[LedgerFlag]]         = uIntDecoder.map(l => BitMask[LedgerFlag](l))
  implicit val decode5: Decoder[BitMask[AccountRootFlag]]    = uIntDecoder.map(l => BitMask[AccountRootFlag](l))
  implicit val decode6: Decoder[BitMask[PaymentChannelFlag]] = uIntDecoder.map(l => BitMask[PaymentChannelFlag](l))

  // GlobalFlag.tfFullyCanonicalSig.value
}

/**
  *  Currently only global flag is fully canonical signature which I always apply prior to submitting.
 **/
sealed abstract class GlobalFlag(val value: Int) extends IntEnumEntry with BitFlag {}

case object GlobalFlag extends IntEnum[GlobalFlag] with IntCirceEnum[GlobalFlag] with BitMaskFlagEnum[GlobalFlag] {
  val values: immutable.IndexedSeq[GlobalFlag] = findValues

  /** Use explicitly included paths only */
  case object tfFullyCanonicalSig extends GlobalFlag(0X80000000)

}

/**
  * Quote:  https://ripple.com/build/transactions/#flags
  * The Flags field can contain various options that affect how a transaction should behave.
  * The options are represented as binary values that can be combined with bitwise-or operations
  * to set multiple flags at once.
  *
  * Most flags only have meaning for a specific transaction type. The same bitwise value may be reused for flags on
  * different transaction types, so it is important to pay attention to the TransactionType field when setting and reading flags.

  */
sealed abstract class PaymentFlag(val value: Int) extends IntEnumEntry with BitFlag

case object PaymentFlag extends IntEnum[PaymentFlag] with IntCirceEnum[PaymentFlag] with BitMaskFlagEnum[PaymentFlag] {

  val values: immutable.IndexedSeq[PaymentFlag] = findValues

  /** Use explicitly included paths only */
  case object tfNoDirectRipple extends PaymentFlag(0X00010000)

  /** Fail txn if can't send the full amount if disabled */
  case object tfPartialPayment extends PaymentFlag(0X00020000)

  /** Enforce input/output quality ratios on txn */
  case object tfLimitQuality extends PaymentFlag(0X00040000)

  case object tfFullyCanonicalSig extends PaymentFlag(0X00080000)

}

sealed abstract class PaymentChannelFlag(val value: Int) extends IntEnumEntry with BitFlag

case object PaymentChannelFlag
    extends IntEnum[PaymentChannelFlag]
    with IntCirceEnum[PaymentChannelFlag]
    with BitMaskFlagEnum[PaymentChannelFlag] {

  /** Clear the expiration time */
  case object tfRenew extends PaymentChannelFlag(0x00010000)

  /** Request to close the channel */
  case object tfClose extends PaymentChannelFlag(0x00020000)

  val values: immutable.IndexedSeq[PaymentChannelFlag] = findValues
}

sealed abstract class LedgerFlag(val value: Int) extends IntEnumEntry with BitFlag

case object LedgerFlag extends IntEnum[LedgerFlag] with IntCirceEnum[LedgerFlag] with BitMaskFlagEnum[LedgerFlag] {

  /** Indicates that the account has used its free SetRegularKey transaction. */
  case object lsfPasswordSpent extends LedgerFlag(0x00010000)

  /** Requires incoming payments to specify a Destination Tag. asfRequireDest */
  case object lsfRequireDestTag extends LedgerFlag(0x00020000)

  /** This account must individually approve other users for those users to hold this account's issuances asfRequireAuth */
  case object lsfRequireAuth extends LedgerFlag(0x00040000)

  /** Client applications should not send XRP to this account. Not enforced by rippled.	asfDisallowXRP */
  case object lsfDisallowXRP extends LedgerFlag(0x00080000)

  /** Disallows use of the master key to sign transactions for this account.	asfDisableMaster */
  case object lsfDisableMaster extends LedgerFlag(0x00100000)

  /** This address cannot freeze trust lines connected to it. Once enabled, cannot be disabled.	asfNoFreeze */
  case object lsfNoFreeze extends LedgerFlag(0x00200000)

  /** All assets issued by this address are frozen.	asfGlobalFreeze */
  case object lsfGlobalFreeze extends LedgerFlag(0x00400000)

  /** Enable rippling on this addresses's trust lines by default. Required for issuing addresses; discouraged for others.	asfDefaultRipple */
  case object lsfDefaultRipple extends LedgerFlag(0x00800000)

  val values: immutable.IndexedSeq[LedgerFlag] = findValues
}

sealed abstract class TrustSetFlag(val value: Int) extends IntEnumEntry with BitFlag

case object TrustSetFlag
    extends IntEnum[TrustSetFlag]
    with IntCirceEnum[TrustSetFlag]
    with BitMaskFlagEnum[TrustSetFlag] {

  /**
    * Authorize the other party to hold issuances from  this account.
    * (No effect unless using the asfRequireAuth AccountSet flag.) Cannot be unset
    */
  case object tfSetfAuth extends TrustSetFlag(0x10000) // 0x10000  65536

  /**
    * Blocks rippling between two trustlines of the same currency, if this flag is set on both.(See No Ripple  for
    * details)
    */
  case object tfSetNoRipple extends TrustSetFlag(0x20000) // 0x20000     131072

  /**
    * Clears the No -Rippling flag.(See No Ripple for details.
    */
  case object tfClearNoRipple extends TrustSetFlag(0x40000) // 0x40000     262144

  /**
    * "Freeze the trustline."
    */
  case object tfSetFreeze extends TrustSetFlag(0x100000) // 0x100,000     1048576

  /**
    * Unfreeze the trustline
    */
  case object tfClearFreeze extends TrustSetFlag(0x200000) // 0x200000    2097152

  val values: immutable.IndexedSeq[TrustSetFlag] = findValues
}

sealed abstract class OfferCreateFlag(val value: Int) extends IntEnumEntry with BitFlag

/**
  * Flags for offers. These are BitMask style flags (or'ed together)
  * https://ripple.com/build/transactions/#offercreate
  */
case object OfferCreateFlag
    extends IntEnum[OfferCreateFlag]
    with IntCirceEnum[OfferCreateFlag]
    with BitMaskFlagEnum[OfferCreateFlag] {

  /**
    *
    *
    */
  case object tfPassive extends OfferCreateFlag(0x00010000)

  /**
    * Never becomes a ledger node, just tries to match current existing orders or fails.
    * (Partial fill I guess(
    */
  case object tfImmediateOrCancel extends OfferCreateFlag(0x00020000)

  /**
    * Same as immediate of cancel, tries to make existing offer but only if full TakerPays amount can be matched.
    * (I think this can be matched across multiple other offers though, test)
    */
  case object tfFillOrKill extends OfferCreateFlag(0x00040000)

  /**
    * Sell all offered even if get more than desired result.  (?)
    */
  case object tfSell extends OfferCreateFlag(0x00080000)

  val values = findValues
}

/** THe Account Root ledger objects contains a BitMask of these flags.
  * To set the flags on account the AccountSetFlag enum is used with AccountSetTx */
sealed abstract class AccountRootFlag(val value: Int) extends IntEnumEntry with BitFlag

/**
  * Flags for offers. These are BitMask style flags (or'ed together)
  * https://xrpl.org/accountroot.html#accountroot-flags
  */
case object AccountRootFlag
    extends IntEnum[AccountRootFlag]
    with IntCirceEnum[AccountRootFlag]
    with BitMaskFlagEnum[AccountRootFlag] {

  case object lsfDefaultRipple  extends AccountRootFlag(0x00800000)
  case object lsfDepositAuth    extends AccountRootFlag(0x01000000)
  case object lsfDisableMaster  extends AccountRootFlag(0x00100000)
  case object lsfDisallowXRP    extends AccountRootFlag(0x00080000)
  case object lsfGlobalFreeze   extends AccountRootFlag(0x00400000)
  case object lsfNoFreeze       extends AccountRootFlag(0x00200000)
  case object lsfPasswordSpent  extends AccountRootFlag(0x00010000)
  case object lsfRequireAuth    extends AccountRootFlag(0x00040000)
  case object lsfRequireDestTag extends AccountRootFlag(0x00020000)

  /**
    * Sell all offered even if get more than desired result.
    */
  case object tfSell extends OfferCreateFlag(0x00080000)

  val values = findValues
}
