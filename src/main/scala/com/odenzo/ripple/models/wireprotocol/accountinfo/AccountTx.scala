package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleScrollingRq, RippleScrollingRs}
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * https://ripple.com/build/rippled-apis/#account-tx
  *
  * Note that ledger_index_min / ledger_index_max OR ledger can be used.
  * TODO: Need to test what happens when we use both and document or enforce disjunction.
  * Note: Binary mode not supported or tested.
  * @param account
  * @param ledger_index_min
  * @param ledger_index_max
  * @param ledger
  * @param binary
  * @param forward
  * @param limit
  * @param marker
  * @param id
  */
case class AccountTxRq(
    account: AccountAddr,
    ledger_index_min: Option[LedgerSequence] = Some(LedgerSequence.WILDCARD_LEDGER),
    ledger_index_max: Option[LedgerSequence] = Some(LedgerSequence.WILDCARD_LEDGER),
    ledger: Option[LedgerIndex] = None,
    binary: Boolean = false,
    forward: Boolean = false,
    limit: Limit = Limit.default,
    marker: Option[Marker] = None,
    id: RippleMsgId = RippleMsgId.random
) extends RippleScrollingRq {}

/**
  * Represents the result= field in AccountTx response.
  * TODO: Sort out where ledger_index_min/max should be. Now in Pagination which is optional.
  * Needs to be optional on response but not request?
  */
case class AccountTxRs(
    account: AccountAddr,
    ledger_index_min: LedgerSequence,
    ledger_index_max: LedgerSequence,
    limit: Option[Limit],
    marker: Option[Marker],
    offset: Option[Long], // Not in request and never seen?
    transactions: List[TransactionRecord],
    validated: Option[Boolean]
) extends RippleScrollingRs

object AccountTxRq extends CirceCodecUtils {
  val command: (String, Json) = "command" -> Json.fromString("account_tx")
  implicit val encoder: Encoder.AsObject[AccountTxRq] = {
    deriveEncoder[AccountTxRq].mapJsonObject(withCommandAndLedgerID("account_tx"))
  }
}

object AccountTxRs {

  // See if we can experiment on errors to get better messages.
  implicit val decoder: Decoder[AccountTxRs] = deriveDecoder[AccountTxRs].handleErrorWith { failure =>
    Decoder.failed(failure) // TODO: Continue to experiment with adding messages in Decoder failurestack/history.
  }

}
