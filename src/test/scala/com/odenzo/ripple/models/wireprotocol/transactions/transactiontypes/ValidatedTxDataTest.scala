package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import com.odenzo.ripple.models.testkit.CodecTesting
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
class ValidatedTxDataTest extends CodecTesting {

  test("Transforming snake_case") {

    val json =
      """
        | {
        |   "ledger_index": 555,
        |   "date" : 100000
        | }
        |""".stripMargin

    val res = for {
      j <- parseAsJson(json)
      o <- j.as[ValidatedTxData]

      j2 = o.asJsonObject
      _  = logger.debug(s"Back to Json:\n${j2.asJson.spaces4}")
    } yield j2
    getOrLog(res)
  }

}
