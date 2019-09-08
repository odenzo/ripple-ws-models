package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe._
import cats._
import cats.data._
import cats.implicits._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxCommon
import com.odenzo.ripple.models.atoms.TxBlob
import com.odenzo.ripple.models.support.RippleEngineResult
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods.TxHistoryRq.wrapCommandCodec
import com.odenzo.ripple.models.wireprotocol.txns.RippleTx

/**
  * TODO: FUTURE: Take out the id random stuff so its pure(r). Default to soem value.
  *
  * [[https://ripple.com/build/rippled-apis/#submit-multisigned]]
  */
case class SubmitMultisignedRq(tx_json: JsonObject, fail_hard: Boolean = false) extends RippleRq

case class SubmitMultisignedRs(
    engine: RippleEngineResult,
    tx_blob: TxBlob,
    tx_json: RippleTx,
    standard: TxCommon
) extends RippleRs

object SubmitMultisignedRq {
  private type ME = SubmitMultisignedRq
  private val command: String            = "submit_multisigned"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object SubmitMultisignedRs {

  implicit val decoder: Decoder[SubmitMultisignedRs] = Decoder.instance { c =>
    val engine   = c.as[RippleEngineResult]
    val tx       = c.get[RippleTx]("tx_json")
    val txcommon = c.get[TxCommon]("tx_json")
    val blob     = c.get[TxBlob]("tx_blob")
    (engine, blob, tx, txcommon).mapN(SubmitMultisignedRs.apply)
  }
  implicit val encoder: Encoder.AsObject[SubmitMultisignedRs] = Encoder.AsObject.instance[SubmitMultisignedRs] {
    c: SubmitMultisignedRs =>
      val fields: List[(String, Json)] =
        List(c.engine.asJsonObject, c.tx_json.asJsonObject, c.standard.asJsonObject)
          .flatMap(_.toList)

      val txBlob = "tx_blob" := c.tx_blob
      JsonObject.fromIterable(txBlob :: fields)

  }
}
