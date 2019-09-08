package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}

/**
  * Cancels an existing book order offer.
  * [[https://ripple.com/build/transactions/#offercancel]]
  *    No txn specific flags.
  */
case class OfferCancelTx(account: AccountAddr, offerSequence: TxnSequence) extends RippleTx

object OfferCancelTx {
  implicit val config: Configuration                = configCapitalize.withDefaults
  private val base: Codec.AsObject[OfferCancelTx]   = deriveConfiguredCodec[OfferCancelTx]
  implicit val codec: Codec.AsObject[OfferCancelTx] = wrapTxnCodec(base, RippleTxnType.OfferCancel)
}
