package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.RippleLogPartition.BASE
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleRs}

/**
  * [[https://ripple.com/build/rippled-apis/#log-level]]
  *  Docs on ripple need correcting too.
  * @param id
  */
case class LogLevelRq(
    severity: Option[RippleLogLevel] = None,
    partition: RippleLogPartition = BASE,
    id: RippleMsgId = RippleMsgId.random
) extends RippleAdminRq

/**
  *
  * @param levels Only there is you don't set a severity level it seems.
  */
case class LogLevelRs(levels: Option[RippleLogLevels]) extends RippleRs

object LogLevelRq {
  val command: (String, Json) = "command" -> "log_level".asJson
  implicit val encoder: Encoder.AsObject[LogLevelRq] = {
    deriveEncoder[LogLevelRq].mapJsonObject(o => command +: o)
  }
}

object LogLevelRs {
  implicit val decoder: Decoder[LogLevelRs] = deriveDecoder[LogLevelRs]
}
