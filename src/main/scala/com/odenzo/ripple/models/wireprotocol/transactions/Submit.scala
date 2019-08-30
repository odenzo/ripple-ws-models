package com.odenzo.ripple.models.wireprotocol.transactions

import cats.implicits._
import io.circe.Decoder.Result
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Json, Encoder, Decoder}

import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxCommon
import com.odenzo.ripple.models.atoms.{TxBlob, RippleMsgId}
import com.odenzo.ripple.models.support.{RippleRs, RippleRq, RippleEngineResult}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction

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
case class SubmitRs(engine: RippleEngineResult, tx_blob: TxBlob, standard: TxCommon, txn: RippleTransaction)
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
    val engine    = c.as[RippleEngineResult]
    val json      = c.get[RippleTransaction]("tx_json")
    val stdSubmit = c.get[TxCommon]("tx_json")
    val blob      = c.get[TxBlob]("tx_blob")
    (engine, blob, stdSubmit, json).mapN(SubmitRs.apply)
  }
}
