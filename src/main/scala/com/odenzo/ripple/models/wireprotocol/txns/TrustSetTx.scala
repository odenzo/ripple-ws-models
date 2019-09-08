package com.odenzo.ripple.models.wireprotocol.txns

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

case class TrustSetTx(
    account: AccountAddr,
    limitAmount: FiatAmount,
    flags: Option[BitMask[TrustSetFlag]] = None,
    qualityIn: Option[Long]              = None,
    qualityOut: Option[Long]             = None
) extends RippleTx

object TrustSetTx extends CirceCodecUtils {
  implicit val config: Configuration             = configCapitalize
  private val base: Codec.AsObject[TrustSetTx]   = deriveConfiguredCodec[TrustSetTx]
  implicit val codec: Codec.AsObject[TrustSetTx] = wrapTxnCodec(base, RippleTxnType.TrustSet)
}
