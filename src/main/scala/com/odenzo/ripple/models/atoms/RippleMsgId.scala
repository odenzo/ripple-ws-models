package com.odenzo.ripple.models.atoms

import java.util.UUID

import io.circe.{Encoder, Decoder}

/**
  * Represents the standard ID field in each Ripple Request Message.
  * Note that this is considered a MANDATORY FIELD in this framework.
  * Even though the field is optional in rippled.
  */
case class RippleMsgId(s: String)

object RippleMsgId {

  val EMPTY: RippleMsgId = RippleMsgId("<No MsgId>")

  /** Cheat, instead of option use this to have a UUID automatically generated on submission */
  val AUTO = RippleMsgId("<<AUTO>>")

  // TODO: Think about making this an Eval or somehow pure, since it really is an IO
  def random = new RippleMsgId(UUID.randomUUID().toString)

  implicit val encoder: Encoder[RippleMsgId] = Encoder.encodeString.contramap[RippleMsgId](_.s)

  /** Decoding the Ripple Message ID can be a number or a String  */
  implicit val decoder: Decoder[RippleMsgId] = {
    Decoder.decodeString
      .or(Decoder.decodeJsonNumber.map(n => n.toString))
      .map(RippleMsgId.apply)

  }
}
