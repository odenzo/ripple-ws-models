package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleScrollingRq, RippleScrollingRs}
import com.odenzo.ripple.models.utils.CirceCodecUtils

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
    ledger: LedgerID = LedgerName.CURRENT_LEDGER,
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

object AccountChannelsRq extends CirceCodecUtils {

  implicit val encoder: Encoder.AsObject[AccountChannelsRq] = {
    deriveEncoder[AccountChannelsRq].mapJsonObject(withCommandAndLedgerID("account_channels"))
  }
}

object AccountChannelsRs {
  implicit val config: Configuration                    = Configuration.default
  implicit val codec: Codec.AsObject[AccountChannelsRs] = deriveConfiguredCodec[AccountChannelsRs]
}
