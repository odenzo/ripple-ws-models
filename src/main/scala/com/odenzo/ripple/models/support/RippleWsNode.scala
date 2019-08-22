package com.odenzo.ripple.models.support

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

/**
  *
  * @param name
  * @param url
  * @param isAdmin
  * @param standlone If we need to manually advance the ledger or its a live node that does consensus by itself
  */
case class RippleWsNode(name: String, url: String, isAdmin: Boolean, standlone: Boolean = false)

object RippleWsNode {
  implicit val decoder: Decoder[RippleWsNode]          = deriveDecoder[RippleWsNode]
  implicit val encoder: Encoder.AsObject[RippleWsNode] = deriveEncoder[RippleWsNode]

  val ripplenet = RippleWsNode(name = "Ripple Production", url = "wss://s2.ripple.com:443", isAdmin = false)
  val testnet   = RippleWsNode(name = "Ripple Test Net", url = "wss://s.altnet.rippletest.net:51233", isAdmin = false)
}
