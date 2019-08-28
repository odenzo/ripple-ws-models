package com.odenzo.ripple.models.atoms
import io.circe._
import io.circe.syntax._
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
  * Miunging.... circe.derivation allow default values now?
  * @param ledger
  */
case class ResultLedger(ledger: Either[LedgerCurrentIndex, (LedgerSequence, LedgerHash)], validated: Boolean = false) {

  def ledgerHash: Option[LedgerHash] = ledger.toOption.map(v => v._2)
  def isCurrentLedger: Boolean       = ledger.isLeft
  def ledgerIndex: LedgerSequence    = ledger.fold(lci => LedgerSequence(lci.v), _._1)
}

object ResultLedger {

  implicit val eEncoder: Encoder.AsObject[Either[LedgerCurrentIndex, (LedgerSequence, LedgerHash)]] = Encoder
    .encodeEither[LedgerCurrentIndex, (LedgerSequence, LedgerHash)]("ledger_current_index", "ledger_index")

  /** This encoder creates a nested object, like "result" = { "x" = 123, "y"=2} instead of lifting up
    *  Would like to solve this, perhaps by looking at deriveUnwrapped
   **/
  implicit val encoder: Encoder.AsObject[ResultLedger] = Encoder.AsObject.instance[ResultLedger] { rl =>
    val varfields = rl.ledger match {
      case Left(lci)          => List("ledger_current_index" := lci)
      case Right((seq, hash)) => List("ledger_index"         := seq, "ledger_hash" := hash)
    }
    JsonObject.fromIterable(varfields).add("validated", rl.validated.asJson)
  }

  //  val d2 = Decoder.decodeEither("leftname", "rightname")
  /**
    * Not this is applied to the result field of an inquiry typically. A helper for the
    * [InquiryName]Rs decoder.
    * It looks for (ledger_current_index) OR (ledger_hash AND ledger_index) fields.
    * These are found directly beneath the result object in responses
    *  See   AccountInfoRs for actual usage in decoding a response
    */
  implicit val decoder: Decoder[ResultLedger] = Decoder.instance[ResultLedger] { hcursor =>
    for {
      validated <- hcursor.getOrElse[Boolean]("validated")(false)
      ledgerDescer <- hcursor.get[LedgerCurrentIndex]("ledger_current_index") match {
        case Right(lci) => (lci.asLeft).asRight
        case Left(_) => // Could not find ledger_current_inde
          for {
            hash  <- hcursor.get[LedgerHash]("ledger_hash")
            index <- hcursor.get[LedgerSequence]("ledger_index")
            validated = ((index, hash).asRight)
          } yield validated
      }
    } yield ResultLedger(ledgerDescer, validated)
  }

}
