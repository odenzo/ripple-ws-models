package com.odenzo.ripple.models.atoms

import io.circe.{Json, JsonObject}

import com.odenzo.ripple.models.testkit.CodecTesting
import io.circe.syntax._
class TxnSequenceTest extends CodecTesting {

  test("decoding") {

    val data = JsonObject.singleton("TxnSequence", Json.fromInt(1)).asJson

    val ans = data.as[TxnSequence]
    logger.info(s"Ans: $ans")
  }
}
