package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.semiauto.deriveEncoder

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  * TODO: Not done at all.
  * Asks Rippled to fetch the ledger if missing.
  * https://ripple.com/build/rippled-apis/#ledger-request
  * This has really strange response stuff (signals error if no ledger but rippled will try and fetch etc).
  * But that is signaled in the result (like a txn error)
  * WARNING: I don't use this, so its just a framework.
  * @param ledger This must be a LedgerHash or LedgerIndex, no named ledgers it seems
  */
case class LedgerRequestRq(ledger: Ledger, id: RippleMsgId = RippleMsgId.random) extends RippleRq
case class LedgerRequestRs(json: Json)                                           extends RippleRs

case class LedgerRequestFailed(acquiring: Option[Json])

case class LedgerRequestPending(
    hash: Option[LedgerHash],
    have_header: Boolean,
    have_state: Option[Boolean],
    have_transaction: Boolean,
    needed_state_hashes: List[RippleHash],
    needed_transaction_hashes: List[TxnHash],
    peers: Long,
    timeouts: Long
)

/** Will actually give ledger information. This may be a duplicate of ledger call, check.
  * This is a Ledger Header https://ripple.com/build/ledger-format/#header-format */
case class LedgerRequestSucceed(ledger: Json, ledger_index: LedgerSequence)

object LedgerRequestRq {
  val command: (String, Json) = "command" -> Json.fromString("ledger_request")
  implicit val encoder: Encoder.AsObject[LedgerRequestRq] = {
    deriveEncoder[LedgerRequestRq]
      .mapJsonObject(o => command +: o)
      .mapJsonObject(o => Ledger.renameLedgerField(o))
  }
}

object LedgerRequestRs {

  implicit val decoder: Decoder[LedgerRequestRs] = Decoder.instance[LedgerRequestRs] { hc =>
    val resjson = hc.focus.getOrElse(Json.Null)
    Right(LedgerRequestRs(resjson))
  }
}
