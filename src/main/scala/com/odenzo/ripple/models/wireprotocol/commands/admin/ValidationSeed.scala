package com.odenzo.ripple.models.wireprotocol.commands.admin

import cats.implicits._
import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}

/**
  *
  * @param secret Validation secret in   base58, RFC-1751, or as a passphrase format technically. None to stop
  *               proposing validations. NO default to avoid cutting off your toes.  Noy
  * @param id
  */
case class ValidationSeedRq(secret: Option[RippleSeedHex] = None, id: RippleMsgId = RippleMsgId.random)
    extends RippleAdminRq

case class ValidationSeedRs(
    validation_key: RippleKey,
    validation_public_key: RipplePublicKey,
    validation_seed: RippleSeed
) extends RippleAdminRs

object ValidationSeedRq extends CirceCodecUtils {
  private type ME = ValidationSeedRq
  private val command: String = "validation_seed"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object ValidationSeedRs {
  import io.circe._
  import io.circe.generic.extras.semiauto._
  implicit val config: Configuration              = Configuration.default
  implicit val decoder: Decoder[ValidationSeedRs] = deriveConfiguredCodec[ValidationSeedRs]
}
