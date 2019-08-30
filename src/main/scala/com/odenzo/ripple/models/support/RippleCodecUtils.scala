package com.odenzo.ripple.models.support

import cats.implicits._
import io.circe.{Json, Decoder}

import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.utils.caterrors.{AppError, AppJsonDecodingError}

/** Going to start using Ripple generic extras now */
trait RippleCodecUtils {

  /**
    * Takes the response Json, which has already been converted from text on wire to valid Json.
    * We then assume it is an RippleGenericResponse envelope, signalling either failure with error
    * or success with a result element correcsponging to the B
    * @param rs
    *  @return  A RippleGenericResponse trait type (RippleGenericSuccess, RippleGenericError
    *
    */
  def decodeGeneric(rs: Json): Either[AppJsonDecodingError, RippleGenericResponse] = {
    CirceUtils.decode[RippleGenericResponse](rs, "RippleGenericResponse")
  }

  /** Takes a RippleGenericSuccess result json and tried to decoder using the
    * decoder.
    *  @param json The result field in a RippleRs
    *  @param decoder Typically Decoder[B] B <: RippleRs
    *  @tparam B
    *  @return
    */
  def decodeResult[B](json: Json, decoder: Decoder[B]): Either[AppJsonDecodingError, B] = {
    CirceUtils.decode(json)(decoder)
  }

  /**
    *  Decodes the reponse message's result field with given decoder if the response is not a generic error
    *   ( i.e.g status="success"
    *  @param json
    *  @param manDecoder
    *  @tparam B
    *  @return     ErrorOr[B] if json parseable as RippleGenericSuccess and can decode to B success else error.
    */
  def decodeFullyOnSuccess[B](json: Json, manDecoder: Decoder[B]): Either[AppError, B] = {
    decodeGeneric(json) match {
      case Right(rs: RippleGenericSuccess) => decodeResult(rs.result, manDecoder)
      case Right(rs: RippleGenericError)   => AppError("Generic Error Not Handled", json).asLeft
      case Left(ex)                        => ex.asLeft
    }
  }

}

object RippleCodecUtils extends RippleCodecUtils
