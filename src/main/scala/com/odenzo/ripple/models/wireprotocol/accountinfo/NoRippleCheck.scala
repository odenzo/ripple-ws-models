package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRs, RippleRq}
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * https://ripple.com/build/rippled-apis/#noripple-check
  * TODO: Not using this yet, but basic round-trip should work.
  * You cannot use "CURRENT" ledger though, because (a) its crazy and it returns ledger_current_index instead of
  * ledger_index
  *
  * @param account   Account Address to check
  * @param role user or gateway
  * @param transactions Generate transaction information to fix any problems (delta from recommendations)
  * @param ledger  Supports both ledger_hash and ledger_index (I hope still)
  * @param limit Number of records to return
  */
case class NoRippleCheckRq(
    account: AccountAddr,
    role: String = "user", // gateway or user
    transactions: Boolean = true,
    ledger: LedgerID = LedgerName.CURRENT_LEDGER,
    limit: Long = 300,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq {

  final def defaultEncoder: Encoder[NoRippleCheckRq] = Encoder[NoRippleCheckRq]
  final def defaultDecoder: Decoder[NoRippleCheckRs] = Decoder[NoRippleCheckRs]
}

/**
  *  Oh, ledger_current_index returned if ask for "current". If ask for validated then get
  *  the standard ldeger_hash and ledger_index. Since I can't think of any reason not to do this on
  *  a valid or old historical ledger, we ignore the ledger_current_index.
  *  This is probably a pattern everywhere in Ripple... "problem" in quotes, because current is not stable?
  *  * @param ledger_index
  * @param resultLedger The ledger the results apply to.
  * @param problems
  * @param transactions
  */
case class NoRippleCheckRs(resultLedger: Option[ResultLedger], problems: List[String], transactions: List[Json])
    extends RippleRs

object NoRippleCheckRq extends CirceCodecUtils {

  implicit val encoder: Encoder.AsObject[NoRippleCheckRq] = {
    deriveEncoder[NoRippleCheckRq].mapJsonObject(withCommandAndLedgerID("noripple_check"))

  }
}

object NoRippleCheckRs {
  implicit val decoder: Decoder[NoRippleCheckRs] = deriveDecoder[NoRippleCheckRs]
    .product(Decoder[ResultLedger])
    .map {
      case (a, theResultLedger) => a.copy(resultLedger = Some(theResultLedger))
    }
}
