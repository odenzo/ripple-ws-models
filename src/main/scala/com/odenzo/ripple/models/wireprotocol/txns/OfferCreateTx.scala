package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}

/**
  * Creates (or modifies) existing book order offer.
  * https://ripple.com/build/transactions/#offercreate
  *    FIXME: We are loosing the "owner_funds" record from the Ledger on inquiry
  * @param expiration
  * @param offerSequence Sequence number of offer to delete before creating this one.
  * @param takerGets
  * @param takerPays
  * @param flags  Read the docs, you should set this manually as important functionality changes
  */
case class OfferCreateTx(
    account: AccountAddr,
    expiration: Option[RippleTime] = None,
    offerSequence: Option[TxnSequence] = None,
    takerGets: CurrencyAmount,
    takerPays: CurrencyAmount,
    flags: BitMask[OfferCreateFlag] = BitMask.empty[OfferCreateFlag] // ACtually should think about this each time
) extends RippleTx

object OfferCreateTx {
  implicit val config: Configuration                = configCapitalize.withDefaults
  private val base: Codec.AsObject[OfferCreateTx]   = deriveConfiguredCodec[OfferCreateTx]
  implicit val codec: Codec.AsObject[OfferCreateTx] = wrapTxnCodec(base, RippleTxnType.OfferCreate)
}
