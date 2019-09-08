package com.odenzo.ripple.models.atoms.ledgertree

import io.circe._
import io.circe.generic.extras.semiauto._

/**
  * Meh, Node Index is 64 char HEX eh? NodeID
  *  It is more like a hash
  * @see LedgerRq for example in the accounts
  * @param v A UInt64 but encoded as String in Json,
  */
case class LedgerNodeIndex(v: String)

object LedgerNodeIndex {

  implicit val codec: Codec[LedgerNodeIndex] = deriveUnwrappedCodec[LedgerNodeIndex]

}
