package com.odenzo.ripple.models.wireprotocol.commands.admin

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.semiauto.deriveDecoder

import com.odenzo.ripple.models.atoms.RippleLogPartition.BASE
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleAdminRq}
import com.odenzo.ripple.models.wireprotocol.commands.admin.LedgerRequestRq.wrapCommandCodec

/**
  * [[https://ripple.com/build/rippled-apis/#log-level]]
  *  Docs on ripple need correcting too.
  */
case class LogLevelRq(severity: Option[RippleLogLevel] = None, partition: RippleLogPartition = BASE)
    extends RippleAdminRq

/**
  *
  * @param levels Only there is you don't set a severity level it seems.
  */
case class LogLevelRs(levels: Option[RippleLogLevels]) extends RippleRs

object LogLevelRq {
  private type ME = LogLevelRq
  private val command: String = "log_level"

  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object LogLevelRs {
  implicit val decoder: Decoder[LogLevelRs] = deriveDecoder[LogLevelRs]
}
