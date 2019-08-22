package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

/**
  * [[https://ripple.com/build/rippled-apis/#validation-create]]
  * This is an admin command
  *  Creates a validation seed. Useful only for admin work. Usable across servers btw.
  * @param secret Secret in word or hash format FIXME: Get the Secret types sorted out. (RFC-1751 or  Ripple Base 58)
  * @param id
  */
case class ValidationCreateRq(secret: Option[RippleSeed] = None, id: RippleMsgId = RippleMsgId.random)
    extends RippleAdminRq

case class ValidationCreateRs(validationKey: ValidationKeys) extends RippleAdminRs

object ValidationCreateRq {
  val command: (String, Json) = "command" -> "validation_create".asJson
  implicit val encoder: Encoder.AsObject[ValidationCreateRq] = {
    deriveEncoder[ValidationCreateRq].mapJsonObject(o => command +: o)
  }
}

object ValidationCreateRs {
  implicit val decoder: Decoder[ValidationCreateRs] = Decoder[ValidationKeys].map(v => ValidationCreateRs(v))
//  implicit val decoder: Decoder[ValidationCreateRs] = Decoder.instance[ValidationKey] { c=>
//      c.as[ValidationKeys].flatMap(ValidationCreateRs)
// }
}
