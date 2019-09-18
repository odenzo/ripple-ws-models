package com.odenzo.ripple.models.support

import cats.implicits._
import io.circe.{JsonObject, Decoder}

import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.utils.caterrors.{ModelsLibError, AppJsonDecodingError}

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
  def decodeGeneric(rs: JsonObject): Either[AppJsonDecodingError, RippleGenericResponse] = {
    CirceUtils.decodeObj[RippleGenericResponse](rs, "RippleGenericResponse")
  }

  /** Takes a RippleGenericSuccess result json and tried to decoder using the
    * decoder.
    *  @param json The result field in a RippleRs
    *  @param decoder Typically Decoder[B] B <: RippleRs
    *  @tparam B
    *  @return
    */
  def decodeResult[B](json: JsonObject, decoder: Decoder[B]): Either[AppJsonDecodingError, B] = {
    CirceUtils.decodeObj(json)(decoder)
  }

  /**
    *  Decodes the reponse message's result field with given decoder if the response is not a generic error
    *   ( i.e.g status="success"
    *  @param rsObj
    *  @param manDecoder
    *  @tparam B
    *  @return     ErrorOr[B] if json parseable as RippleGenericSuccess and can decode to B success else error.
    */
  def decodeFullyOnSuccess[B](rsObj: JsonObject, manDecoder: Decoder[B]): Either[ModelsLibError, B] = {
    import io.circe.syntax._
    decodeGeneric(rsObj) match {
      case Right(rs: RippleGenericSuccess) => decodeResult(rs.result, manDecoder)
      case Right(rs: RippleGenericError)   => ModelsLibError("Generic Error Not Handled", rsObj.asJson).asLeft
      case Left(ex)                        => ex.asLeft
    }
  }

}

object RippleCodecUtils extends RippleCodecUtils
