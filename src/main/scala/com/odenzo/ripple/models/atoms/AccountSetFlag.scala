package com.odenzo.ripple.models.atoms

import scala.collection.immutable

import enumeratum.values.{IntEnumEntry, IntEnum, IntCirceEnum}

/**
  * AccountFlags used (mostly/only?) in AccountSet. Not these are <em>not bit-mask kind of flags</em>
  * aka AccountSetFlags These should be used for AccountSet instead of the older tf Flags
  */
sealed abstract class AccountSetFlag(val value: Int) extends IntEnumEntry

case object AccountSetFlag extends IntEnum[AccountSetFlag] with IntCirceEnum[AccountSetFlag] {

  val values: immutable.IndexedSeq[AccountSetFlag] = findValues

  case object asfNOOP extends AccountSetFlag(0)

  /** Require a destination tag to send txn to this account. */
  case object asfRequireDest extends AccountSetFlag(1)

  /** Require authorization for users to hold balances issued */
  case object asfRequireAuth extends AccountSetFlag(2)

  /** XRP should not be sent to this account. */
  case object asfDisallowXRP extends AccountSetFlag(3)

  /** Disallow use of the master key*/
  case object asfDisableMaster extends AccountSetFlag(4)

  /** Track the ID of this account's most recent txn */
  case object asfAccountTxnID extends AccountSetFlag(5)

  /** Permanently give up the ability to freeze trust lines or disable Global Freeze*/
  case object asfNoFreeze extends AccountSetFlag(6)

  /** "Freeze all assets issued by this account*/
  case object asfGlobalFreeze extends AccountSetFlag(7)

  /** Enable rippling on this account's trust lines by default */
  case object asfDefaultRipple extends AccountSetFlag(8)

}
