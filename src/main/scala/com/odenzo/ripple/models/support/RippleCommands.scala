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
  * I see no good way to attempt to do this, other than just not do it.
  *  T<: RippleRq U<:RippleRs and hard code the encoders and decoders in base object.
  *  This is deprecated

  */
@deprecated("Goung Away", "2019")
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
@deprecated("Goung Away", "2019")
case class RqRsCodec[A <: RippleRq, B <: RippleRs](
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

/**
  * Bit of a hack until Scala 3 conversion
  */
@deprecated("Goung Away", "2019")
object Commands {

  def isAdminCmd(rq: RippleRq): Boolean = rq.isInstanceOf[RippleAdminRq]
  def isAdminCmd(rq: RippleRs): Boolean = rq.isInstanceOf[RippleAdminRs]

  def isSrollingCmd(rq: RippleRq): Boolean = rq.isInstanceOf[RippleScrollingRq]
  def isScrolingCmd(rq: RippleRs): Boolean = rq.isInstanceOf[RippleScrollingRs]

  // Account Info Section Commands

  val accountOffersCmd     = RqRsCodec(Encoder.AsObject[AccountOffersRq], Decoder[AccountOffersRs])
  val accountTxCmd         = RqRsCodec(Encoder.AsObject[AccountTxRq], Decoder[AccountTxRs])
  val gatewayBalancesCmd   = RqRsCodec(Encoder.AsObject[GatewayBalancesRq], Decoder[GatewayBalancesRs])
  val noRippleCheckCmd     = RqRsCodec(Encoder.AsObject[NoRippleCheckRq], Decoder[NoRippleCheckRs])
  val walletProposeCmd     = RqRsCodec(Encoder.AsObject[WalletProposeRq], Decoder[WalletProposeRs])
  val feeCmd               = RqRsCodec(Encoder.AsObject[FeeRq], Decoder[FeeRs])
  val accountChannels      = RqRsCodec(Encoder.AsObject[AccountChannelsRq], Decoder[AccountChannelsRs])
  val accountInfoCmd       = RqRsCodec(Encoder.AsObject[AccountInfoRq], Decoder[AccountInfoRs])
  val accountCurrenciesCmd = RqRsCodec(Encoder.AsObject[AccountCurrenciesRq], Decoder[AccountCurrenciesRs])
  val accountLinesCmd      = RqRsCodec(Encoder.AsObject[AccountLinesRq], Decoder[AccountLinesRs])
  val accountObjectsCmd    = RqRsCodec(Encoder.AsObject[AccountObjectsRq], Decoder[AccountObjectsRs])

  // Convenience Commands
  val connectCmd = RqRsCodec(Encoder.AsObject[ConnectRq], Decoder[ConnectRs])
  val pingCmd    = RqRsCodec(Encoder.AsObject[PingRq], Decoder[PingRs])
  val stopCmd    = RqRsCodec(Encoder.AsObject[StopRq], Decoder[StopRs])

  // object LedgerInfo {
  val ledgerCmd        = RqRsCodec(Encoder.AsObject[LedgerRq], Decoder[LedgerRs])
  val ledgerAcceptCmd  = RqRsCodec(Encoder.AsObject[LedgerAcceptRq], Decoder[LedgerAcceptRs])
  val ledgerCleanerCmd = RqRsCodec(Encoder.AsObject[LedgerCleanerRq], Decoder[LedgerCleanerRs])
  val ledgerClosedCmd  = RqRsCodec(Encoder.AsObject[LedgerClosedRq], Decoder[LedgerClosedRs])
  val ledgerCurrentCmd = RqRsCodec(Encoder.AsObject[LedgerCurrentRq], Decoder[LedgerCurrentRs])
  val ledgerDataCmd    = RqRsCodec(Encoder.AsObject[LedgerDataRq], Decoder[LedgerDataRs])
  val ledgerEntryCmd   = RqRsCodec(Encoder.AsObject[LedgerEntryRq], Decoder[LedgerEntryRs])
  val ledgerRequestCmd = RqRsCodec(Encoder.AsObject[LedgerRequestRq], Decoder[LedgerRequestRs])

  //object serverinfo {

  val canDeleteCmd = RqRsCodec(Encoder.AsObject[CanDeleteRq], Decoder[CanDeleteRs])

  val consensusInfoCmd = RqRsCodec(Encoder.AsObject[ConsensusInfoRq], Decoder[ConsensusInfoRs])

  val featureCmd = RqRsCodec(Encoder.AsObject[FeatureRq], Decoder[FeatureRs])

  //val feeCmd = Codec(Encoder[FeeRq], Decoder[FeeRs])

  val fetchInfoCmd = RqRsCodec(Encoder.AsObject[FetchInfoRq], Decoder[FetchInfoRs])

  val getCountsCmd = RqRsCodec(Encoder.AsObject[GetCountsRq], Decoder[GetCountsRs])

  val logLevelCmd = RqRsCodec(Encoder.AsObject[LogLevelRq], Decoder[LogLevelRs])

  val logRotateCmd = RqRsCodec(Encoder.AsObject[LogRotateRq], Decoder[LogRotateRs])

  val serverInfoCmd = RqRsCodec(Encoder.AsObject[ServerInfoRq], Decoder[ServerInfoRs])

  val serverStateCmd = RqRsCodec(Encoder.AsObject[ServerStateRq], Decoder[ServerStateRs])

  val validationCreateCmd = RqRsCodec(Encoder.AsObject[ValidationCreateRq], Decoder[ValidationCreateRs])

  val validationSeedCmd = RqRsCodec(Encoder.AsObject[ValidationSeedRq], Decoder[ValidationSeedRs])

  // object subscription {

  /* Subcriptions are special cases and not considered commands  */

  /** Commands under transactions except Sign and Submit. Not the actual txjson payloads are modelled differently. */
  // object transactions {

  val bookOffersCmd = RqRsCodec(Encoder.AsObject[BookOffersRq], Decoder[BookOffersRs])

  val ripplePathFindCmd = RqRsCodec(Encoder.AsObject[RipplePathFindRq], Decoder[RipplePathFindRs])

  val txCmd = RqRsCodec(Encoder.AsObject[TxRq], Decoder[TxRs])

  val submitCmd = RqRsCodec(Encoder.AsObject[SubmitRq], Decoder[SubmitRs])

  val signCmd = RqRsCodec(Encoder.AsObject[SignRq], Decoder[SignRs])

  val signForCmd = RqRsCodec(Encoder.AsObject[SignForRq], Decoder[SignForRs])

}
