package com.odenzo.ripple.models.wireprotocol

import io.circe.generic.extras.Configuration
import io.circe.{Encoder, JsonObject, Decoder, _}
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
import monocle.macros.Lenses

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.txns.RippleTx

/** This represents a fully formed *unsigned* transaction that can be signed.
  * It is also possible to  post-processes to "auto-fill" some options like Sequence or Fee
  **/
case class RippleTxnRq(tx: RippleTx, opt: TxOptions)

// TODO: Interim doesn't deal with Ripple Engine Errors
case class RippleTxnRs(tx: RippleTx, common: CommonTxnRs)

object RippleTxnRq {

  implicit val encoder: Encoder.AsObject[RippleTxnRq] =
    Encoder.AsObject.instance[RippleTxnRq] { a: RippleTxnRq =>
      val baseIter = RippleTx.encoder.encodeObject(a.tx).toIterable
      JsonObject.fromIterable(baseIter ++ a.opt.asJsonObject.toIterable)
    }

  implicit val decoder: Decoder[RippleTxnRq] = {
    Decoder.instance[RippleTxnRq] { hc =>
      for {
        base <- hc.as[RippleTx]
        opt  <- hc.as[TxOptions]
      } yield RippleTxnRq(base, opt)
    }
  }
}

/**  Decodes items from Json primary, a R/O type structure.
  *  This is in additional to seperately decoded Ripple Engine Result (possibly errors) and the root txn.
  *  This is part of the response to sign (and submit)
  *
  *   The signers information is currently not included in this OR the TxOptions as work through
  *   Signers and multisigners.
  *   The mapping of this is painful.
  *
  *
  *   Well, when do we actually use this. When we do a SignRq/Rs or SubmitRq/Rs
  *   In both cases we have parsed out everything in result already except for tx_json
  *   So, essentially this is TxOptions, but with hash and any signing/TxnSignature/SigningPubKey data.
  *
 **/
@Lenses("_") case class CommonTxnRs(
    sequence: TxnSequence, // capitalize field name
    hash: RippleHash,      // The hash of the trasnaction
    fee: Drops = Drops.stdFee, // Rq / Rs
    accountTxnID: Option[TxnHash],
    signingPubKey: SigningPublicKey,    // Will be ""  if Multisigning
    txnSignature: Option[TxnSignature], // None if multisigning
    signers: Option[Signers],           // None if not multisigning
    lastLedgerSequence: Option[LedgerSequence],
    memos: Option[Memos] = None // Rq / Rs
) {}

object CommonTxnRs extends CirceCodecUtils {

  implicit val config: Configuration              = configCapitalizeExcept(Set("hash")).withDefaults
  implicit val codec: Codec.AsObject[CommonTxnRs] = deriveConfiguredCodec[CommonTxnRs]
}
