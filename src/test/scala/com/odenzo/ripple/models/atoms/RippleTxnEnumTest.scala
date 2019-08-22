package com.odenzo.ripple.models.atoms

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import com.odenzo.ripple.models.testkit.CodecTesting

class RippleTxnEnumTest extends CodecTesting {

  test("Encoding") {

    RippleTxnType.values.foreach(v => logger.info(s"Value: $v => ${v.asJson}"))

    val decoded: Either[Error, RippleTxnType] =
      io.circe.parser.parse(""" "AccountSetTxn"  """).flatMap(_.as[RippleTxnType])
    logger.info(s"Decoded: $decoded")

    val simple: Json = RippleTxnType.SetRegularKey.asJson
    logger.info(s"Plain Enough JSON: ${simple.noSpaces}")
  }
}
