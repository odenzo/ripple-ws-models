package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  * Used to calculate Gateway balances by excluding certain accounts (hot wallets)
  * Good way to see what issuers have issued basically.
  * https://ripple.com/build/rippled-apis/#gateway-balances
  * TODO: Only partially implemented on response side, experiment and see what the Objects arr
  */
case class GatewayBalancesRq(
    account: AccountAddr,
    strict: Boolean = true,
    hotwallet: Seq[AccountAddr] = Seq.empty[AccountAddr],
    ledger: LedgerID = LedgerName.VALIDATED_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq {

  final def defaultEncoder: Encoder[GatewayBalancesRq] = Encoder[GatewayBalancesRq]

  final def defaultDecoder: Decoder[GatewayBalancesRs] = Decoder[GatewayBalancesRs]
}

case class GatewayBalancesRs(
    account: AccountAddr,
    resultLedger: Option[ResultLedger],
    obligations: Option[Json],
    balances: Option[Json],
    assets: Option[Json]
) extends RippleRs

object GatewayBalancesRq {
  val command: (String, Json) = "command" -> Json.fromString("gateway_balances")
  implicit val encoder: Encoder.AsObject[GatewayBalancesRq] = {
    deriveEncoder[GatewayBalancesRq].mapJsonObject(o => command +: o)
  }
}

object GatewayBalancesRs {
  implicit val decoder: Decoder[GatewayBalancesRs] = deriveDecoder[GatewayBalancesRs]
    .product(Decoder[ResultLedger])
    .map {
      case (a, theResultLedger) =>
        a.copy(resultLedger = Some(theResultLedger))
    }
}
