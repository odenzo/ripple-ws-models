package com.odenzo.ripple.models.support

import cats.implicits._
import io.circe.generic.semiauto._
import io.circe.{Decoder, Json}

import com.odenzo.ripple.models.atoms._

sealed trait RippleGenericResponse {
  def id: RippleMsgId
  def status: String
}

/**
  *   We could try and parameterize this on result, which is usually T <: RippleRs
  *   I choose not to because for some use cases I jsut do partial decoding to get what I need.
  * @param id
  * @param status
  * @param result The JSON in the result field that is specific to the type of response
  */
case class RippleGenericSuccess(id: RippleMsgId, status: String, result: Json) extends RippleGenericResponse {

  assert(status.equals("success"), "status must be success")

}

object RippleGenericSuccess {
  implicit val successDecoder: Decoder[RippleGenericSuccess] = deriveDecoder[RippleGenericSuccess]

}

/**
  *   Hmmm, submit is giving me error and error_exception at top level Generic (not engine level)
  * @param id
  * @param status
  * @param error
  * @param error_code
  * @param error_message
  * @param request  The raw JSON request which caused this response.
  */
case class RippleGenericError(
    id: RippleMsgId,
    status: String,
    error: String,
    error_code: Long,
    error_message: String,
    request: Json
) extends RippleGenericResponse {

  assert(status.equals("error"), "statys must be error")
  // Extension point for handling complication error transaction codes etc.
  val errors: RippleResponseError = RippleResponseError(error, error_code, error_message)

}

/**
  *  Alternative final encoding of a RippleResonse that is not scrollable, for now Scrollable as NEList of RippleAnswer
  * @param id
  * @param status
  * @param ans
  * @tparam B
  */
case class RippleAnswer[B <: RippleRs](id: RippleMsgId, status: String, ans: Either[RippleGenericError, B])

object RippleGenericError {

  implicit val errorDecoder: Decoder[RippleGenericError] = deriveDecoder[RippleGenericError]
}

/** This covers Transaction and Command responses from Rippled, but not the subscription events/messages.
  */
object RippleGenericResponse {

  /** Decode to either Success or Error as right or a decoding failure as left */
  implicit val decoder: Decoder[RippleGenericResponse] =
    List[Decoder[RippleGenericResponse]](
      Decoder[RippleGenericSuccess].widen,
      Decoder[RippleGenericError].widen
    ).reduceLeft(_ or _)

  // I thought
  val orDecoder: Decoder[RippleGenericResponse] =
    Decoder[RippleGenericSuccess].widen
      .or(Decoder[RippleGenericError].widen)

  val eitherDecoder: Decoder[Either[RippleGenericSuccess, RippleGenericError]] =
    Decoder[RippleGenericSuccess].widen.either(Decoder[RippleGenericError].widen)

}
