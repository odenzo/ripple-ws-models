package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

case class AccountCurrenciesRq(
    account: AccountAddr,
    strict: Boolean = true,
    ledger: Ledger = LedgerName.CURRENT_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq {}

case class AccountCurrenciesRs(
    resultLedger: Option[ResultLedger],
    receive_currencies: List[Currency],
    send_currencies: List[Currency],
    validated: Boolean = false
) extends RippleRs

object AccountCurrenciesRq {
  val command: (String, Json) = "command" -> Json.fromString("account_currencies")
  implicit val encoder: Encoder.AsObject[AccountCurrenciesRq] = {
    deriveEncoder[AccountCurrenciesRq]
      .mapJsonObject(o => command +: o)
      .mapJsonObject(o => Ledger.renameLedgerField(o))
  }

}

object AccountCurrenciesRs {
  implicit val decoder: Decoder[AccountCurrenciesRs] = {
    deriveDecoder[AccountCurrenciesRs].product(Decoder[ResultLedger]).map {
      case (a, theResultLedger) => a.copy(resultLedger = Some(theResultLedger))
    }
  }
}
