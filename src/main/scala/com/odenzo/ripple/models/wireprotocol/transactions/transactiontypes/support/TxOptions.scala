package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support

import io.circe.generic.extras.Configuration
import monocle.macros.Lenses

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  *  Some options to apply when *constructing* a RippleTransaction Request (e.g. for Signing)
  * * This corresponds to standard fields in a transaction that has been signed OR submitted, but not yet validated.
  * * Used to build and submit transactions
  *
  * @param fee  Fee to use, alternative would be to let Ripple server do it, but not good with local signing anyway
  * @param memos
  * @param sequence Same as Fee, this should always be set manually now, but for transition None will let sewrver
  *                 autofill when signing on server
  * @param lastLedgerSequence
  */
@Lenses
case class TxOptions(
    fee: Drops = Drops.stdFee, // Rq / Rs
    sequence: Option[TxnSequence] = None,
    accountTxnID: Option[TxnHash] = None,                    // ??
    lastLedgerSequence: LedgerSequence = LedgerSequence.MAX, // Instead of option. Should always be set reasonably.
    memos: Option[Memos] = None,                             // Rq / Rs
    destinationTag: Option[UInt32] = None                    // DestiantionTag?
)

object TxOptions {

  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val config: Configuration            = CirceCodecUtils.capitalizeConfig
  implicit val codec: Codec.AsObject[TxOptions] = deriveConfiguredCodec[TxOptions]

}
