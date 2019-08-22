package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Encoder, Json, JsonObject}

import com.odenzo.ripple.models.atoms.RippleMsgId
import com.odenzo.ripple.models.support.RippleRq

/**
  * TODO: FUTURE: Take out the id random stuff so its pure(r). Default to soem value.
  *
  * [[https://ripple.com/build/rippled-apis/#submit-multisigned]]
  */
case class Submit_Multisigned(tx_json: JsonObject, fail_hard: Boolean = false, id: RippleMsgId = RippleMsgId.EMPTY)
    extends RippleRq

object Submit_Multisigned {

  val command: (String, Json) = "command" -> Json.fromString("submit_multisigned")
  implicit val encoder: Encoder.AsObject[Submit_Multisigned] = {
    deriveEncoder[Submit_Multisigned].mapJsonObject(o => command +: o)
  }

}
