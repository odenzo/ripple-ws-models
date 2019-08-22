package com.odenzo.ripple.models.support

import cats.implicits._
import io.circe.{Decoder, Encoder, Json, JsonObject}
import scribe.Logging

import com.odenzo.ripple.models.utils.caterrors.CatsTransformers.ErrorOr
import com.odenzo.ripple.models.wireprotocol.accountinfo._
import com.odenzo.ripple.models.wireprotocol.conviencefunctions._
import com.odenzo.ripple.models.wireprotocol.ledgerinfo._
import com.odenzo.ripple.models.wireprotocol.serverinfo._
import com.odenzo.ripple.models.wireprotocol.transactions._

/** First cut at a dependant type with two type parameters, one for request and one for response.
  *
  *  @tparam F
  */
trait CommandContext {

  /** Tyoe of the request,    subtype of RippleRq or RippleAdminRq */
  type Request
  // type Resposne

  /* Type of the expected response object. Generally should be a subtype RippleRs  */
  //type U

}

/** Baby steps to build an inquiry typeclass (for testing, but slim downable for production)
  *  Not quite a normal typeclass, as instances of these are really just setting the decoders.
  *  There are no implicit conversions or lifters.
  *  Main purpose here is to let people override things (e.g. decoders) for special cases
  *
  */
/** A Command is a request/response type pattern in Rippled, no transaction path or subscriptions.
  *  This is a generic framework to define a command in terms of a RippleRq and RippleRs subclass really.
  *  But now it is unbounded by type heirarchy.
  *  This trait I want to implement somehow as a TypeClass that can do A -> B via IO (Calling Rippled Server async)
  *
  *  @tparam A The type of the request object,
  *  @tparam B The type of the response object
  */
case class Codec[A <: RippleRq, B <: RippleRs](
    encoder: Encoder.AsObject[A],
    decoder: Decoder[B]
) extends Logging {

  /** Dead simple encoding of the request object. Should never fail */
  def encode(rq: A): JsonObject = encoder.encodeObject(rq)

  /** Production style decoding, given the end result of an error. A RippleError is also shifted
    * to an Error response
    **/
  def decode(rs: Json): ErrorOr[RippleAnswer[B]] = {
    // An error decoding or a GenericSuccess or GenericError
    val generic: ErrorOr[RippleGenericResponse] = RippleCodecUtils.decodeGeneric(rs)

    generic.flatMap {
      case e: RippleGenericError => RippleAnswer(e.id, e.status, e.asLeft[B]).asRight

      case RippleGenericSuccess(id, status, result) =>
        val ok: ErrorOr[B]              = RippleCodecUtils.decodeResult(result, decoder)
        val a: ErrorOr[RippleAnswer[B]] = ok.map(r => RippleAnswer(id, status, r.asRight))
        a
    }

  }

  /** Decodes result as RippleGenericResponse WITHOUT checking the status */
  def decodeGeneric(rs: Json): ErrorOr[RippleGenericResponse] = {
    RippleCodecUtils.decodeGeneric(rs)
  }

  def decodeResult(rgrs: RippleGenericSuccess): ErrorOr[B] = RippleCodecUtils.decodeResult(rgrs.result, decoder)

}

object Codec {}

/**
  * Bit of a hack until Scala 3 conversion
  */
object Commands {

  def isAdminCmd(rq: RippleRq): Boolean = rq.isInstanceOf[RippleAdminRq]
  def isAdminCmd(rq: RippleRs): Boolean = rq.isInstanceOf[RippleAdminRs]

  def isSrollingCmd(rq: RippleRq): Boolean = rq.isInstanceOf[RippleScrollingRq]
  def isScrolingCmd(rq: RippleRs): Boolean = rq.isInstanceOf[RippleScrollingRs]

  // Account Info Section Commands

  val accountOffersCmd     = Codec(Encoder.AsObject[AccountOffersRq], Decoder[AccountOffersRs])
  val accountTxCmd         = Codec(Encoder.AsObject[AccountTxRq], Decoder[AccountTxRs])
  val gatewayBalancesCmd   = Codec(Encoder.AsObject[GatewayBalancesRq], Decoder[GatewayBalancesRs])
  val noRippleCheckCmd     = Codec(Encoder.AsObject[NoRippleCheckRq], Decoder[NoRippleCheckRs])
  val walletProposeCmd     = Codec(Encoder.AsObject[WalletProposeRq], Decoder[WalletProposeRs])
  val feeCmd               = Codec(Encoder.AsObject[FeeRq], Decoder[FeeRs])
  val accountChannels      = Codec(Encoder.AsObject[AccountChannelsRq], Decoder[AccountChannelsRs])
  val accountInfoCmd       = Codec(Encoder.AsObject[AccountInfoRq], Decoder[AccountInfoRs])
  val accountCurrenciesCmd = Codec(Encoder.AsObject[AccountCurrenciesRq], Decoder[AccountCurrenciesRs])
  val accountLinesCmd      = Codec(Encoder.AsObject[AccountLinesRq], Decoder[AccountLinesRs])
  val accountObjectsCmd    = Codec(Encoder.AsObject[AccountObjectsRq], Decoder[AccountObjectsRs])

  // Convenience Commands
  val connectCmd = Codec(Encoder.AsObject[ConnectRq], Decoder[ConnectRs])
  val pingCmd    = Codec(Encoder.AsObject[PingRq], Decoder[PingRs])
  val stopCmd    = Codec(Encoder.AsObject[StopRq], Decoder[StopRs])

  // object LedgerInfo {
  val ledgerCmd        = Codec(Encoder.AsObject[LedgerRq], Decoder[LedgerRs])
  val ledgerAcceptCmd  = Codec(Encoder.AsObject[LedgerAcceptRq], Decoder[LedgerAcceptRs])
  val ledgerCleanerCmd = Codec(Encoder.AsObject[LedgerCleanerRq], Decoder[LedgerCleanerRs])
  val ledgerClosedCmd  = Codec(Encoder.AsObject[LedgerClosedRq], Decoder[LedgerClosedRs])
  val ledgerCurrentCmd = Codec(Encoder.AsObject[LedgerCurrentRq], Decoder[LedgerCurrentRs])
  val ledgerDataCmd    = Codec(Encoder.AsObject[LedgerDataRq], Decoder[LedgerDataRs])
  val ledgerEntryCmd   = Codec(Encoder.AsObject[LedgerEntryRq], Decoder[LedgerEntryRs])
  val ledgerRequestCmd = Codec(Encoder.AsObject[LedgerRequestRq], Decoder[LedgerRequestRs])

  //object serverinfo {

  val canDeleteCmd = Codec(Encoder.AsObject[CanDeleteRq], Decoder[CanDeleteRs])

  val consensusInfoCmd = Codec(Encoder.AsObject[ConsensusInfoRq], Decoder[ConsensusInfoRs])

  val featureCmd = Codec(Encoder.AsObject[FeatureRq], Decoder[FeatureRs])

  //val feeCmd = Codec(Encoder[FeeRq], Decoder[FeeRs])

  val fetchInfoCmd = Codec(Encoder.AsObject[FetchInfoRq], Decoder[FetchInfoRs])

  val getCountsCmd = Codec(Encoder.AsObject[GetCountsRq], Decoder[GetCountsRs])

  val logLevelCmd = Codec(Encoder.AsObject[LogLevelRq], Decoder[LogLevelRs])

  val logRotateCmd = Codec(Encoder.AsObject[LogRotateRq], Decoder[LogRotateRs])

  val serverInfoCmd = Codec(Encoder.AsObject[ServerInfoRq], Decoder[ServerInfoRs])

  val serverStateCmd = Codec(Encoder.AsObject[ServerStateRq], Decoder[ServerStateRs])

  val validationCreateCmd = Codec(Encoder.AsObject[ValidationCreateRq], Decoder[ValidationCreateRs])

  val validationSeedCmd = Codec(Encoder.AsObject[ValidationSeedRq], Decoder[ValidationSeedRs])

  // object subscription {

  /* Subcriptions are special cases and not considered commands  */

  /** Commands under transactions except Sign and Submit. Not the actual txjson payloads are modelled differently. */
  // object transactions {

  val bookOffersCmd = Codec(Encoder.AsObject[BookOffersRq], Decoder[BookOffersRs])

  val ripplePathFindCmd = Codec(Encoder.AsObject[RipplePathFindRq], Decoder[RipplePathFindRs])

  val txCmd = Codec(Encoder.AsObject[TxRq], Decoder[TxRs])

  val submitCmd = Codec(Encoder.AsObject[SubmitRq], Decoder[SubmitRs])

  val signCmd = Codec(Encoder.AsObject[SignRq], Decoder[SignRs])

  val signForCmd = Codec(Encoder.AsObject[SignForRq], Decoder[SignForRs])

}
