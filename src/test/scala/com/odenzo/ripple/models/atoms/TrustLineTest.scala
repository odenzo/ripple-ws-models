package com.odenzo.ripple.models.atoms

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting

class TrustLineTest extends CodecTesting {

  val jsonStr =
    """
      |   {
      |      "account" : "rPaXbAJbZ3HyAxuDRDLCxrPtzmC5Pt3Tz2",
      |      "balance" : "55554",
      |      "currency" : "NZD",
      |      "limit" : "99999999",
      |      "limit_peer" : "0",
      |      "quality_in" : 0,
      |      "quality_out" : 0
      |    }
      |""".stripMargin

  val result = for {
    jobj <- parseAsJObj(jsonStr)
    obj  <- decodeObj(jobj, "Missing Default")(Decoder[TrustLine])
  } yield obj
  val r: TrustLine = testCompleted(result)
  logger.debug(s"RES: ${pprint.apply(r)}")
}
