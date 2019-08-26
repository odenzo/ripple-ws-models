package com.odenzo.ripple.models.support

import cats.implicits._
import io.circe.{Decoder, Encoder, Json}

import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.utils.caterrors.CatsTransformers.ErrorOr
import com.odenzo.ripple.models.utils.caterrors.{AppJsonDecodingError, OError}

/** Going to start using Ripple generic extras now */
trait RippleCodecUtils {

  /**
    * Utility for dynamically applying encoder to an object.
    * Typically A <: RippleRq in our situation but who cares.
    **/
  def encode[A](rq: A, encoder: Encoder[A]): Json = encoder.apply(rq)

  /**
    * Takes the response Json, which has already been converted from text on wire to valid Json.
    * We then assume it is an RippleGenericResponse envelope, signalling either failure with error
    * or success with a result element correcsponging to the B
    * @param rs
    *  @return  A RippleGenericResponse trait type (RippleGenericSuccess, RippleGenericError
    *
    */
  def decodeGeneric(rs: Json): ErrorOr[RippleGenericResponse] = {
    // This is ADT. Here is is doing to use the implicit in scope Decoder.
    rs.as[RippleGenericResponse]
      .leftMap(new AppJsonDecodingError(rs, _, "RippleGenericResponse"))
  }

  /** Takes a RippleGenericSuccess result json and tried to decoder using the
    * decoder.
    *  @param json
    *  @param decoder Typically Decoder[B] B <: RippleRs
    *  @tparam B
    *  @return
    */
  def decodeResult[B](json: Json, decoder: Decoder[B]): ErrorOr[B] = {
    val decode: Either[AppJsonDecodingError, B] = CirceUtils.decode(json, decoder)
    decode
  }

  /**
    *  Decodes the result field with given decoder is not generic error
    *  @param json
    *  @param manDecoder
    *  @tparam B
    *  @return     ErrorOr[B] if json parseable as RippleGenericSuccess and can decode to B success else error.
    */
  def decodeFullyOnSuccess[B](json: Json, manDecoder: Decoder[B]): ErrorOr[B] = {
    decodeGeneric(json) match {
      case Right(rs: RippleGenericSuccess) => decodeResult(rs.result, manDecoder)
      case Right(rs: RippleGenericError)   => OError("Generic Error Not Handled").asLeft
      case Left(ex)                        => ex.asLeft
    }
  }

}

object RippleCodecUtils extends RippleCodecUtils
