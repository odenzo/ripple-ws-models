package com.odenzo.ripple.models.atoms
import io.circe._
import io.circe.generic.extras.semiauto._
import cats._
import cats.data._
import cats.implicits._

/**
  * Many inquiry responses 'result' return ledger_index and ledger_hash and validated.
  * Except, if we are inquiring on 'current' ledger (that is mutable still) then
  * only ledger_current_index is returned.
  * All results have an optional validated inside the result in addition to the generic response.
  * If its there and true its a validated ledger. If not there, then its not a validated ledger.
  * (from which the information is source). A validated ledger will always have a LedgerIndex and LedgerHash.
  * Oddly, things like AccountChannelsRq don't return ledger at all.
  *
  *  Tempted to move ledger_hash out as an optional to play with Encoder.encodeEither(f1,f1)
  * @param ledger
  */
case class ResultLedger(ledger: Either[LedgerCurrentIndex, LedgerSequence]) {
  def ledgerIndex: LedgerSequence = ledger match {
    case Left(LedgerCurrentIndex(v)) => LedgerSequence(v)
    case Right(ls)                   => ls
  }
}

object ResultLedger {

  implicit val eCodec: Codec.AsObject[Either[LedgerCurrentIndex, LedgerSequence]] =
    Codec.codecForEither[LedgerCurrentIndex, LedgerSequence]("ledger_current_index", "ledger_index")

  val codec: Codec[ResultLedger]              = deriveUnwrappedCodec[ResultLedger]
  implicit val decoder: Decoder[ResultLedger] = codec.prepare(_.up)

  // When encoding this you will get a nested JsonObject that needs to be lifted up at call-site.s
  implicit val encoder: Encoder[ResultLedger] = codec
}
