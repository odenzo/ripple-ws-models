package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.LedgerHeader
import com.odenzo.ripple.models.support.{RippleRs, RippleRq}
import com.odenzo.ripple.models.utils.CirceCodecUtils

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
    accounts: Boolean = false,
    expand: Boolean = false,
    owner_funds: Boolean = true,
    ledger: LedgerID = LedgerName.VALIDATED_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq

/**
  *
  * @param ledger    Overview information
  * @param ledger_hash  ledger_hash
  * @param ledger_index ledger_index, usually not used on current
  */
case class LedgerRs(ledger: LedgerHeader, ledger_hash: LedgerHash, ledger_index: LedgerSequence) extends RippleRs

object LedgerRq extends CirceCodecUtils {
  implicit val encoder: Encoder.AsObject[LedgerRq] = deriveEncoder[LedgerRq]
    .mapJsonObject(withCommandAndLedgerID("ledger"))

}

object LedgerRs {
  implicit val decoder: Decoder[LedgerRs] = deriveDecoder[LedgerRs]
}
