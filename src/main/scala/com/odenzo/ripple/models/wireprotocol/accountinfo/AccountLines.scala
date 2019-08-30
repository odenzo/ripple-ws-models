package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleScrollingRq, RippleScrollingRs}
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  *  Used to get the trust lines and their balances. For currencies other than XRP.
  *  For XRP balance see AccountInfo
  * @param account
  * @param peer      Limit lines to those between account the this account address
  * @param limit    Ask (but not demand) rippled limit the number of results to this.
  * @param marker    Scrolling marker (opaque)
  * @param ledger    Get account lines as of this point in history, defaults to last validated ledger
  * @param id
  */
case class AccountLinesRq(
    account: Account,
    peer: Option[AccountAddr] = None,
    limit: Limit = Limit.default,
    marker: Option[Marker] = None,
    ledger: LedgerID = LedgerName.CURRENT_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleScrollingRq

/** The result field portion for account_lines command response.
  * Add the optional ledger_index , ledger_current_index, ledger_hash */
case class AccountLinesRs(
    account: AccountAddr,
    lines: List[TrustLine],
    resultLedger: Option[ResultLedger],
    marker: Option[Marker]
) extends RippleScrollingRs

object AccountLinesRq extends CirceCodecUtils {
  implicit val encoder: Encoder.AsObject[AccountLinesRq] = {
    deriveEncoder[AccountLinesRq].mapJsonObject(withCommandAndLedgerID("account_lines"))
  }

}

object AccountLinesRs {
  implicit val decoder: Decoder[AccountLinesRs] = deriveDecoder[AccountLinesRs]
    .product(Decoder[ResultLedger])
    .map {
      case (a, theResultLedger) => a.copy(resultLedger = Some(theResultLedger))
    }
}
