package com.odenzo.ripple.models.wireprotocol.serverinfo

import cats.implicits._
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

/**
  *
  * @param secret Validation secret in   base58, RFC-1751, or as a passphrase format technically. None to stop
  *               proposing validations. NO default to avoid cutting off your toes.  Noy
  * @param id
  */
case class ValidationSeedRq(secret: Option[RippleSeedHex] = None, id: RippleMsgId = RippleMsgId.random)
    extends RippleAdminRq

case class ValidationSeedRs(key: RippleKey, pubKey: RipplePublicKey, seed: RippleSeed) extends RippleAdminRs

object ValidationSeedRq {
  val command: (String, Json) = "command" -> "validation_seed".asJson
  implicit val encoder: Encoder.AsObject[ValidationSeedRq] = {
    deriveEncoder[ValidationSeedRq].mapJsonObject(o => command +: o)
  }
}

object ValidationSeedRs {
  implicit val decoder: Decoder[ValidationSeedRs] = Decoder.instance[ValidationSeedRs] { cursor =>
    val res: Result[ValidationSeedRs] =
      (
        cursor.get[RippleKey]("validation_key"),
        cursor.get[RipplePublicKey]("validation_public_key"),
        cursor.get[RippleSeed]("validation_seed")
      ).mapN(ValidationSeedRs(_, _, _))
    res
  }
}
