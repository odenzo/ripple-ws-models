package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRs, RippleRq}
import com.odenzo.ripple.models.utils.CirceCodecUtils

case class AccountCurrenciesRq(
    account: AccountAddr,
    strict: Boolean = true,
    ledger: LedgerID = LedgerName.CURRENT_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq {}

case class AccountCurrenciesRs(
    resultLedger: Option[ResultLedger],
    receive_currencies: List[Currency],
    send_currencies: List[Currency],
    validated: Boolean = false
) extends RippleRs

object AccountCurrenciesRq extends CirceCodecUtils {
  implicit val encoder: Encoder.AsObject[AccountCurrenciesRq] = {
    deriveEncoder[AccountCurrenciesRq].mapJsonObject(withCommandAndLedgerID("account_currencies"))
  }

}

object AccountCurrenciesRs {

  import io.circe._
  import io.circe.generic.extras.semiauto._
  implicit val config: Configuration = Configuration.default.withDefaults
  implicit val decoder: Decoder[AccountCurrenciesRs] = {
    deriveConfiguredDecoder[AccountCurrenciesRs].product(Decoder[ResultLedger]).map {
      case (a, theResultLedger) => a.copy(resultLedger = Some(theResultLedger))
    }
  }
}
