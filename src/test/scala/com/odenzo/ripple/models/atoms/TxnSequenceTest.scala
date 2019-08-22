package com.odenzo.ripple.models.atoms

import io.circe.{Json, JsonObject}

import com.odenzo.ripple.models.testkit.CodecTesting

class TxnSequenceTest extends CodecTesting {

  test("decoding") {

    val data = JsonObject.singleton("TxnSequence", Json.fromInt(1))

    val ans = TxnSequence.decoder.decodeJson(Json.fromInt(1))
    logger.info(s"Ans: $ans")
  }
}
