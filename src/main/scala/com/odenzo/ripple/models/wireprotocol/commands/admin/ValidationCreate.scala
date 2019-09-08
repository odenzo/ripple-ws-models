package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.commands.{RippleAdminRs, RippleAdminRq}
import com.odenzo.ripple.models.wireprotocol.commands.admin.LedgerRequestRq.wrapCommandCodec

/**
  * [[https://ripple.com/build/rippled-apis/#validation-create]]
  * This is an admin command
  *  Creates a validation seed. Useful only for admin work. Usable across servers btw.
  * @param secret In word format.
  * @param id
  */
case class ValidationCreateRq(secret: Option[RippleKey] = None, id: RippleMsgId = RippleMsgId.random)
    extends RippleAdminRq

case class ValidationCreateRs(validationKey: ValidationKeys) extends RippleAdminRs

object ValidationCreateRq {
  private type ME = ValidationCreateRq
  private val command: String = "validation_create"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object ValidationCreateRs {
  import io.circe._
  import io.circe.generic.extras.semiauto._
  implicit val config: Configuration            = Configuration.default
  implicit val codec: Codec[ValidationCreateRs] = deriveUnwrappedCodec[ValidationCreateRs]

  // TODO: TEST ME. ValidationKeys is at top level.
}
