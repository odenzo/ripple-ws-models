package com.odenzo.ripple.models.wireprotocol.transactions

import cats.implicits._
import io.circe.Decoder.Result
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, Encoder, Json}

import com.odenzo.ripple.models.atoms.{RippleMsgId, TxBlob}
import com.odenzo.ripple.models.support.{RippleEngineResult, RippleRq, RippleRs}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{PendingTxData, RippleTransaction}

/**
  * See RippleGenericResponse, RippleTxnRsStatus and the other pile of mess to be sorted out for response envelopes
  * @param tx_blob The transaction blob from signing the transaction
  */
case class SubmitRq(tx_blob: TxBlob, fail_hard: Boolean = true, id: RippleMsgId = RippleMsgId.random) extends RippleRq

/**
  * Usually on a submit we get a result= that contains a tx_blob (don't care), a tx_json and the engine_result stuff.
  * This is used JUST for SubmitResult and should be called that actually.
  * And it is also the same thing as the Sign Result field.
  * We could sniff or something...
  * TODO: Would be nice to see if a SubmitRs[T] could be used. Don't think so, even if explicitly mentions,
  * like    json.as[SubmitRs[AccountSetTx]] TODO: Worth trying.
  * @param txn This is the original submission normally there?
  * @param tx_blob On submitting we are submitting the blob, not sure if ALWAYS there on error cases.
  * @param engine  The Ripple Engine results
  */
case class SubmitRs(engine: RippleEngineResult, tx_blob: TxBlob, standard: PendingTxData, txn: RippleTransaction)
    extends RippleRs

object SubmitRq {

  val command: (String, Json) = "command" -> Json.fromString("submit")
  implicit val encoder: Encoder.AsObject[SubmitRq] = {
    deriveEncoder[SubmitRq].mapJsonObject(o => command +: o)
  }

}

object SubmitRs {

  /** This is applied to the Result of the GenericResponse structure.
    **/
  implicit val decoder: Decoder[SubmitRs] = Decoder.instance { c =>
    val engine: Result[RippleEngineResult] = c.as[RippleEngineResult]
    val json: Result[RippleTransaction]    = c.get[RippleTransaction]("tx_json")
    val stdSubmit                          = c.get[PendingTxData]("tx_json")
    val blob: Result[TxBlob]               = c.get[TxBlob]("tx_blob")
    (engine, blob, stdSubmit, json).mapN(SubmitRs.apply)
  }
}
