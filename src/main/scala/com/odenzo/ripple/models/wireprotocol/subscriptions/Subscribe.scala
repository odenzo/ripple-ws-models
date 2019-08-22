package com.odenzo.ripple.models.wireprotocol.subscriptions

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._

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

  val command: (String, Json) = "command" -> Json.fromString("subscribe")
  implicit val encoder: Encoder.AsObject[SubscribeLedgerRq] = {
    deriveEncoder[SubscribeLedgerRq].mapJsonObject(o => command +: o)
  }

}

object LedgerClosedMsg {

  /**
    * Drops are not in "0023" format they are unquoted modified Drops decoder to handle both instead of using
    * Decoder.prepare
    */
  implicit val decoder: Decoder[LedgerClosedMsg] = deriveDecoder[LedgerClosedMsg]
//  implicit val decode: Decoder[LedgerClosedMsg] = new Decoder[LedgerClosedMsg] {
//    override def apply(c: HCursor): Result[LedgerClosedMsg] = {
//      for {
//        base             <- c.get[BigInt]("fee_base").map(v => Drops(v))
//        ref              <- c.get[BigInt]("fee_ref").map(Drops(_))
//        lhash            <- c.get[RippleHash]("ledger_hash")
//        lindex           <- c.get[LedgerIndex]("ledger_index")
//        ltime            <- c.get[RippleTime]("ledger_time")
//        resbase          <- c.get[BigInt]("reserve_base").map(Drops(_))
//        resinc           <- c.get[BigInt]("reserve_inc").map(Drops(_))
//        txncount         <- c.getOrElse[Long]("txn_count")(0L)
//        validatedLedgers <- c.get[String]("validated_ledgers")
//      } yield LedgerClosedMsg(base, ref, lhash, lindex, ltime, resbase, resinc, txncount, validatedLedgers)
//    }
//  }
}
