package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

/**
  * https://ripple.com/build/rippled-apis/#wallet-propose
  * I should use Either but not strongly typed to make clear. Waiting for Scala upgrade
  * @param key_type  Recommend to leave as default
  */
case class WalletProposeRq(
    seed: Option[String] = None,
    passphrase: Option[String] = None,
    key_type: Option[String] = None,
    id: RippleMsgId = RippleMsgId.random
) extends RippleAdminRq {}

case class WalletProposeRs(keys: AccountKeys) extends RippleAdminRs

object WalletProposeRq {
  val command: (String, Json) = "command" -> Json.fromString("wallet_propose")
  implicit val encoder: Encoder.AsObject[WalletProposeRq] = {
    deriveEncoder[WalletProposeRq].mapJsonObject(o => command +: o)
  }
}

object WalletProposeRs {
  implicit val decoder: Decoder[WalletProposeRs] = Decoder[AccountKeys].map(keys => WalletProposeRs(keys))
}
