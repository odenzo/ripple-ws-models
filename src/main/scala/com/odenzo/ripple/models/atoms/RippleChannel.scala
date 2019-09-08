package com.odenzo.ripple.models.atoms

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

/**
  * Represents a Ripple Payment Channel, as found in AccountChannels inquiry reponse
  * Details at: https://ripple.com/build/rippled-apis/#account-channels
  */
case class RippleChannel(
    account: AccountAddr,
    amount: Drops,
    balance: Drops,
    channel_id: ChannelIndex,
    destination_account: AccountAddr,
    public_key: Option[RipplePublicKey],
    settle_delay: Int,
    expiration: Option[RippleTime],
    cancel_after: Option[RippleTime],
    source_tag: Option[UInt32], // TODO: Make more speciifc., this id dt=llll
    destination_tag: Option[UInt32]
)

object RippleChannel {
  implicit val config: Configuration                = Configuration.default
  implicit val codec: Codec.AsObject[RippleChannel] = deriveConfiguredCodec[RippleChannel]

}
