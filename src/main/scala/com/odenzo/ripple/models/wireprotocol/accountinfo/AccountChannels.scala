package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleScrollingRq, RippleScrollingRs}

/**
  * https://ripple.com/build/rippled-apis/#account-channels
  *
  * @param account
  * @param destination_account
  * @param ledger
  * @param id
  * @param marker
  */
case class AccountChannelsRq(
    account: AccountAddr,
    destination_account: Option[AccountAddr],
    ledger: Ledger = LedgerName.CURRENT_LEDGER,
    id: RippleMsgId = RippleMsgId.random,
    limit: Limit = Limit(50),
    marker: Option[Marker] = None
) extends RippleScrollingRq {
  def scrollWith(marker: Marker): AccountChannelsRq = this.copy(marker = Option(marker))

}

case class AccountChannelsRs(
    account: AccountAddr,
    channels: List[RippleChannel],
    limit: Option[Limit],
    marker: Option[Marker]
) extends RippleScrollingRs

object AccountChannelsRq {

  val command: (String, Json) = "command" -> Json.fromString("account_channels")
  implicit val encoder: Encoder.AsObject[AccountChannelsRq] = {
    deriveEncoder[AccountChannelsRq]
      .mapJsonObject(o => command +: o)
      .mapJsonObject(o => Ledger.renameLedgerField(o))

  }
}

object AccountChannelsRs {
  implicit val decoder: Decoder[AccountChannelsRs] = deriveDecoder[AccountChannelsRs]

}
