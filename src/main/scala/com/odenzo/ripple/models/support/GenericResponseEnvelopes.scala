package com.odenzo.ripple.models.support

import cats.implicits._
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe._

import com.odenzo.ripple.models.atoms._

sealed trait RippleGenericResponse {
  def id: Option[RippleMsgId]
  def status: String
}

/**
  *   We could try and parameterize this on result, which is usually T <: RippleRs
  *   I choose not to because for some use cases I jsut do partial decoding to get what I need.
  * @param id
  * @param status
  * @param result The JSON in the result field that is specific to the type of response
  */
case class RippleGenericSuccess(id: Option[RippleMsgId], status: String, result: JsonObject)
    extends RippleGenericResponse {
  assert(status.equals("success"), "status must be success")
}

object RippleGenericSuccess {
  val codec                                                    = deriveCodec[RippleGenericSuccess]
  implicit val decoder: Decoder[RippleGenericSuccess]          = codec.ensure(_.status === "success", "Not Success")
  implicit val encoder: Encoder.AsObject[RippleGenericSuccess] = codec
}

/**
  *   Hmmm, submit is giving me error and error_exception at top level Generic (not engine level)
  */
case class RippleGenericError(
    id: Option[RippleMsgId],
    status: String,
    error: String,
    error_code: Long,
    error_message: String,
    request: JsonObject
) extends RippleGenericResponse {

  assert(status.equals("error"), "statys must be error")

}

object RippleGenericError {
  val codec                                                  = deriveCodec[RippleGenericError]
  implicit val decoder: Decoder[RippleGenericError]          = codec.ensure(_.status === "error", "Not an error")
  implicit val encoder: Encoder.AsObject[RippleGenericError] = codec
}

/** This covers Transaction and Command responses from Rippled, but not the subscription events/messages.
  */
object RippleGenericResponse {

  implicit val encoder: Encoder.AsObject[RippleGenericResponse] = Encoder.AsObject.instance[RippleGenericResponse] {
    case b: RippleGenericSuccess => b.asJsonObject
    case b: RippleGenericError   => b.asJsonObject

  }

  /** Decode to either Success or Error as right or a decoding failure as left */
  implicit val decoder: Decoder[RippleGenericResponse] =
    List[Decoder[RippleGenericResponse]](
      Decoder[RippleGenericSuccess].widen,
      Decoder[RippleGenericError].widen
    ).reduceLeft(_ or _)

  // I thought
  val orDecoder: Decoder[RippleGenericResponse] =
    Decoder[RippleGenericSuccess].widen or Decoder[RippleGenericError].widen

  val eitherDecoder: Decoder[Either[RippleGenericSuccess, RippleGenericError]] =
    Decoder[RippleGenericSuccess].widen.either(Decoder[RippleGenericError].widen)

}
