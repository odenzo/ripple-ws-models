package com.odenzo.ripple.models.wireprotocol.subscriptions

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

/** *
  * Subscriptions are a bit complicated, so I made a seperate request for each kind of subscription.
  * Starting with LedgerClosed to monitor the validation cycles.
  */
trait SubscribeRq

/**
  * Subscribes to get notification every time a new ledger is validated. Admin only.
  * https://ripple.com/build/rippled-apis/#subscribe
  * This is a refinement of the general SubscribeRq for my immediate hacky use case.
  */
case class SubscribeLedgerRq(streams: List[String] = List("ledger"), id: RippleMsgId = RippleMsgId.random)
    extends SubscribeRq

case class SubscrubeLedgerRs()

/** Once a ledger subscription is running is sends these message.
  * The first response is simple a generic inquire with result field equal to LedgerClosedMsg
  * Ignoring the "type" = "ledgerClosed" which is present in message/event but not initial response.
  */
case class LedgerClosedMsg(
    fee_base: Drops,
    fee_ref: Drops,
    ledger_hash: RippleHash,
    ledger_index: LedgerSequence,
    ledger_time: RippleTime,
    reserve_base: Drops,
    reserve_inc: Drops,
    txn_count: Option[Long],  // Because not in intiial response result
    validated_ledgers: String // TODO: Maybe a LedgerIndexRange type? LI-LI
)

object SubscribeLedgerRq {

  val command: (String, Json)                               = "command" -> Json.fromString("subscribe")
  implicit val config                                       = Configuration.default
  val codec: Codec.AsObject[SubscribeLedgerRq]              = deriveConfiguredCodec[SubscribeLedgerRq]
  implicit val encoder: Encoder.AsObject[SubscribeLedgerRq] = codec.mapJsonObject(o => command +: o)
  implicit val decoder: Decoder[SubscribeLedgerRq]          = codec
}

object LedgerClosedMsg {
  implicit val config: Configuration                  = Configuration.default
  implicit val codec: Codec.AsObject[LedgerClosedMsg] = deriveConfiguredCodec[LedgerClosedMsg]
}
