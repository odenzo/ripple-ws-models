package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.{Decoder, Codec}

import com.odenzo.ripple.models.atoms.Drops
import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * This one is not in docs, but appears sporadically. Like amendments, validators adverstise over intervals I guess.
  * Think never deltas for this so can remove all Option things?
  *
  * https://ripple.com/build/transactions/#setfee has some info.
  * @param baseFee   Charge of a transaction BEFORE load scaling applied. HexString sorry
  * @param referenceFeeUnits Cost of transaction BEFORE scaling in Fee Units. Guess a FeeUnit = 1 Drop? as it is 10 too
  *                          Although documentation implies Drops = referenceFeeUnits * baseFee (maybe I am loaded
  *                          locally)
  */
case class FeeSettingsNode(
    baseFee: Option[String],          // Hex value of 00000A in String now, so this must be Drops as Unit at least.
    flags: Option[Long],              // zero so far.
    referenceFeeUnits: Option[Drops], // Note this is a long not String, but I think is drops.
    reserveBase: Option[Drops],
    reserveIncrement: Option[Drops], // Note sure if this is drops, or  this * FeeUnits = Drops, a Long Looks like Drops
    index: Option[LedgerNodeIndex]
) extends LedgerNode {
  def baseFeeAsDrops: Option[Drops] =
    baseFee.map(s => BigInt(s"0x$s")).map(d => Drops(d)) // FIXME: Unsafe Java Exception evils.
}

object FeeSettingsNode {
  implicit val config: Configuration                  = CirceCodecUtils.capitalizeExcept(Set("index"))
  implicit val codec: Codec.AsObject[FeeSettingsNode] = deriveConfiguredCodec[FeeSettingsNode]
}
