package com.odenzo.ripple.models.atoms.ledgertree

import io.circe.{Decoder, Encoder}

/**
  * Meh, Node Index is 64 char HEX eh? NodeID
  *  It is more like a hash
  * @see LedgerRq for example in the accounts
  * @param v A UInt64 but encoded as String in Json,
  */
case class LedgerNodeIndex(v: String)

object LedgerNodeIndex {

  implicit val decoder: Decoder[LedgerNodeIndex] = Decoder.decodeString.map(LedgerNodeIndex.this(_))
  implicit val encoder: Encoder[LedgerNodeIndex] = Encoder.encodeString.contramap[LedgerNodeIndex](_.v)
}
