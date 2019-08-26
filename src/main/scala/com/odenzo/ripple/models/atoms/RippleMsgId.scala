package com.odenzo.ripple.models.atoms

import java.util.UUID

import io.circe.{Decoder, Encoder}

/**
  * Represents the standard ID field in each Ripple Request Message.
  * Note that this is considered a MANDATORY FIELD in this framework.
  * Even though the field is optional in rippled.
  */
case class RippleMsgId(s: String)

object RippleMsgId {

  val EMPTY: RippleMsgId = RippleMsgId("<No MsgId>")

  // TODO: Think about making this an Eval?
  def random = new RippleMsgId(UUID.randomUUID().toString)

  implicit val encoder: Encoder[RippleMsgId] = Encoder.encodeString.contramap[RippleMsgId](_.s)

  /** Decoding the Ripple Message ID can be a number or a String  */
  implicit val decoder: Decoder[RippleMsgId] = {
    Decoder.decodeString
      .map(RippleMsgId.apply)
      .or(Decoder.decodeJsonNumber.map(n => RippleMsgId(n.toString)))

  }
}
