package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods
import cats._
import cats.data._
import cats.implicits._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.{Codec, Encoder, JsonObject, Decoder}

import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxCommon
import com.odenzo.ripple.models.atoms.{TxBlob, RippleMsgId}
import com.odenzo.ripple.models.support.RippleEngineResult
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}

/**
  * See RippleGenericResponse, RippleTxnRsStatus and the other pile of mess to be sorted out for response envelopes
  *  This is special case and doesn't follow normal pattern. No OptionTx or CommonCmdRq
  * @param tx_blob The transaction blob from signing the transaction
  */
case class SubmitRq(tx_blob: TxBlob, fail_hard: Boolean = true, id: RippleMsgId = RippleMsgId.random) extends RippleRq

/**
  *  This maps to the result field
  * This is somewhat out of sync of how I do things. And also engine should be in TxCommon same level.
  *  THinking a better approach is pull out the tx_json and standard into wrapper to keep this
  *  more like the standard commands. Perhaps even just have SubmiRs(jsonObject)
  *   TODO: Really no reason not to have the CommonTxRs in here.
  *
  * @param tx_json This is the original submission with possible errors
  * @param tx_blob On submitting we are submitting the blob, always there.s
  * @param engine  The Ripple Engine results, either positive or negaticess
  */
case class SubmitRs(engine: RippleEngineResult, tx_blob: TxBlob, tx_json: JsonObject) extends RippleRs

object SubmitRq extends CirceCodecUtils {
  private type ME = SubmitRq
  private val command: String            = "submit"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object SubmitRs {

  /** This is applied to the Result of the GenericResponse structure.
    **/
  implicit val decoder: Decoder[SubmitRs] = Decoder.instance[SubmitRs] { c =>
    val engine   = c.as[RippleEngineResult]
    val blob     = c.get[TxBlob]("tx_blob")
    val tx       = c.get[JsonObject]("tx_json")
    val txcommon = c.get[TxCommon]("tx_json")

    (engine, blob, tx).mapN(SubmitRs.apply)
  }

  implicit val encoder: Encoder.AsObject[SubmitRs] = Encoder.AsObject.instance[SubmitRs] { a =>
    import io.circe.syntax._
    val fields = ("tx_blob" := a.tx_blob) :: a.engine.asJsonObject.toList ::: a.tx_json.toList
    JsonObject.fromFoldable(fields)
  }
}
