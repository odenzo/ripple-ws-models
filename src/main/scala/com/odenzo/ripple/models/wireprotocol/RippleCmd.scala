package com.odenzo.ripple.models.wireprotocol

import io.circe.generic.extras.Configuration
import monocle.macros.Lenses
import cats._
import cats.data._
import cats.implicits._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.utils.caterrors.AppError
import com.odenzo.ripple.models.wireprotocol.commands.RippleRq

/** All ripple commands either succeed or fail and don't need signing.
  * Build the RippleRq directly from implementing commands.
  * Unfortunately, I loose the subclass T due to my struggles with Circe.
  * Most of the time you only need the response object though, as the request
  * is in the response if there was a failure.
  *  */
@Lenses("_") case class RippleCmdRq(rq: JsonObject, common: CommonCmdRq) {}

object RippleCmdRq extends CirceCodecUtils {

  implicit val encoder: Encoder.AsObject[RippleCmdRq] = Encoder.AsObject.instance[RippleCmdRq] { a =>
    val fields = a.rq.toIterable ++ a.common.asJsonObject.toIterable
    JsonObject.fromIterable(fields)
  }
}

@Lenses("_") case class RippleCmdRs[T](cmdRs: T, common: CommonCmdRs)

object RippleCmdRs {}

/**
  *  Common Command Options.
  * @param id
  * @param ledger_index  Set to LedgerName or LedgerSequence, default to LedgerName.CURRENT_LEDGER. Setting this
  *                      unsets ledger_hash
  * @param ledger_hash   Setting this will unset ledger_index. Used to determine which ledger the command operates on.
  * @param limit
  * @param marker
  */
@Lenses("_") case class CommonCmdRq(
    id: RippleMsgId                   = RippleMsgId.EMPTY,
    ledger_index: Option[LedgerIndex] = LedgerName.CURRENT_LEDGER.some,
    ledger_hash: Option[LedgerHash]   = None,
    limit: Option[Limit]              = None,
    marker: Option[Marker]            = None
) {

  def bindCmd[T <: RippleRq: Encoder.AsObject](rq: T) = RippleCmdRq(rq.asJsonObject, this)

  def withLedgerIndex(s: LedgerIndex): CommonCmdRq = CommonCmdRq.setLedger(s)(this)
  def withLedgerHash(s: LedgerHash): CommonCmdRq   = CommonCmdRq.setLedgerHash(s)(this)
  def withLimit(l: Limit): CommonCmdRq             = this.copy(limit = l.some)
  def withId(id: RippleMsgId): CommonCmdRq         = this.copy(id = id)
}

object CommonCmdRq {

  final val default: CommonCmdRq                  = CommonCmdRq()
  final val defaultWithValidated                  = CommonCmdRq(ledger_index = LedgerName.VALIDATED_LEDGER.some)
  implicit val config: Configuration              = Configuration.default
  implicit val codec: Codec.AsObject[CommonCmdRq] = deriveConfiguredCodec[CommonCmdRq]

  def setLedger(l: LedgerIndex)(cmd: CommonCmdRq): CommonCmdRq = {
    (_ledger_index.set(l.some) andThen _ledger_hash.set(None))(cmd)
  }
  def setLedgerHash(l: LedgerHash)(cmd: CommonCmdRq): CommonCmdRq = {
    (_ledger_index.set(None) andThen _ledger_hash.set(l.some))(cmd)
  }

}

/**  Decodes items from Json result object, a R/O type structure
  *   Note that the id, status fields are in the enclosing object and parsed as RippleGenericResponsse
  *    Also note that for SignRs we do not extract the signers information
  * */
@Lenses("_") case class CommonCmdRs(
    id: Option[RippleMsgId],
    ledger_index: Option[LedgerSequence],
    ledger_current_index: Option[LedgerSequence],
    ledger_hash: Option[LedgerHash], // Is this really a leadger hash, or just hash of txn
    validated: Boolean = false
) {

  def ledgerSequence: Either[AppError, LedgerSequence] =
    AppError.required(ledger_index orElse ledger_current_index, "ledger_index AND ledger_current_index blank")
}

object CommonCmdRs {
  val empty: CommonCmdRs                          = CommonCmdRs(None, None, None, None, false)
  implicit val config: Configuration              = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[CommonCmdRs] = deriveConfiguredCodec[CommonCmdRs]
}
