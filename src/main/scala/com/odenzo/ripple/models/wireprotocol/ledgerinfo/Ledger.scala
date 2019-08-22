package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.LedgerHeader
import com.odenzo.ripple.models.support.{RippleRq, RippleRs}

/**
  * https://ripple.com/build/rippled-apis/#ledger
  * @todo This is pretty basic
  *       TODO: Lots more options here. Will never enable full. Owner funds and expand we need
  *       though
  *
  * Some options options return lots of data and necisitate running on admin socket.
  * This will not be automatically enforced. // TODO: Future, filter match on Json and flag is non-admin socket in
  * comms module -- probably better to put a doesRequireAdmin(json):Boolean function here.
  *
  * [[https://ripple.com/build/rippled-apis/#ledger]]
  *
  * @param transactions
  * @param accounts
  * @param ledger
  */
case class LedgerRq(
    transactions: Boolean = true,
    accounts: Boolean = true,
    ledger: Ledger = LedgerName.VALIDATED_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq

/**
  *
  * @param ledger
  * @param resultLedger Standard to level hash/index/validated
  */
case class LedgerRs(ledger: LedgerHeader, resultLedger: Option[ResultLedger]) extends RippleRs

object LedgerRq {
  val command: (String, Json) = "command" -> "ledger".asJson
  implicit val encoder: Encoder.AsObject[LedgerRq] = deriveEncoder[LedgerRq]
    .mapJsonObject(o => command +: o)
    .mapJsonObject(o => Ledger.renameLedgerField(o))

}

object LedgerRs {
  implicit val decoder: Decoder[LedgerRs] = deriveDecoder[LedgerRs]
    .product(Decoder[ResultLedger])
    .map {
      case (a, theResultLedger) => a.copy(resultLedger = Some(theResultLedger))
    }
}
