package com.odenzo.ripple.models.support

import io.circe.Decoder.Result
import io.circe.syntax._

import com.odenzo.ripple.models.testkit.CodecTesting

class TxnStatusCodeTest extends CodecTesting {

  test("Codec") {

    val v: TxnStatusCode = TxnStatusCode.tecCLAIM

    val json                     = v.asJson
    val o: Result[TxnStatusCode] = json.as[TxnStatusCode]
    logger.debug(s"JSON: ${json.spaces4}  for $v   and $o")
    o shouldEqual (Right(v))
    o.map(_.asJson) shouldEqual Right(json)

  }

}
