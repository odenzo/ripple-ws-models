package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleScrollingRq, RippleScrollingRs}

/**
  * https://ripple.com/build/rippled-apis/#account-tx
  *
  * NOTE: Note that ledger_index_min / ledger_index_max OR ledger can be used. Currently this is not enforced.
  * TODO: Need to test what happens when we use both and document or enforce disjunction.
  * Note: Binary mode not supported or tested.
  * @param account
  * @param ledger_index_min
  * @param ledger_index_max
  * @param binary
  * @param forward
  */
case class AccountTxRq(
    account: AccountAddr,
    ledger_index_min: Option[LedgerSequence] = Some(LedgerSequence.WILDCARD_LEDGER),
    ledger_index_max: Option[LedgerSequence] = Some(LedgerSequence.WILDCARD_LEDGER),
    binary: Boolean                          = false,
    forward: Boolean                         = false
) extends RippleScrollingRq

/**
  * Represents the result= field in AccountTx response.
  * TODO: This doesn't handle binary results for transactions
  */
case class AccountTxRs(
    account: AccountAddr,
    ledger_index_min: LedgerSequence,
    ledger_index_max: LedgerSequence,
    offset: Option[Long], // Not in request and never seen?
    transactions: List[TransactionRecord]
) extends RippleScrollingRs

object AccountTxRq extends CirceCodecUtils {

  private type ME = AccountTxRq
  private val command: String = "account_tx"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object AccountTxRs {

  // See if we can experiment on errors to get better messages.
  implicit val codec: Codec.AsObject[AccountTxRs] = deriveCodec[AccountTxRs]
}
