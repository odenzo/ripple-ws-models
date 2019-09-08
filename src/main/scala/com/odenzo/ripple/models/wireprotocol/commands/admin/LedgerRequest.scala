package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}

/**
  * TODO: Not done at all.
  * Asks Rippled to fetch the ledger if missing.
  * https://ripple.com/build/rippled-apis/#ledger-request
  * This has really strange response stuff (signals error if no ledger but rippled will try and fetch etc).
  * But that is signaled in the result (like a txn error)
  * WARNING: I don't use this, so its just a framework.
  */
case class LedgerRequestRq()           extends RippleRq
case class LedgerRequestRs(json: Json) extends RippleRs

/** Will actually give ledger information. This may be a duplicate of ledger call, check.
  * This is a Ledger Header https://ripple.com/build/ledger-format/#header-format */
case class LedgerRequestSucceed(ledger: Json, ledger_index: LedgerSequence)

object LedgerRequestRq extends CirceCodecUtils {
  private type ME = LedgerRequestRq
  private val command: String = "ledger_request"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object LedgerRequestRs {

  import io.circe._
  import io.circe.generic.extras.semiauto._
  implicit val config: Configuration             = Configuration.default
  implicit val decoder: Decoder[LedgerRequestRs] = deriveConfiguredCodec[LedgerRequestRs]
}

// This class isn't used much so I don't worry about filling in details.
// But the request can result in
// 1)   Failed
// 2)  In Progress - ledger getting fetched
//
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
