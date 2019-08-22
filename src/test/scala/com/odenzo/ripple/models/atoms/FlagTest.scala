package com.odenzo.ripple.models.atoms

import com.odenzo.ripple.models.testkit.CodecTesting

class FlagTest extends CodecTesting {

  import io.circe.syntax._

  test("Encode/Decode TxnFlags") {
    PaymentFlag.values.foreach { f: PaymentFlag =>
      logger.info(s"$f => ${f.asJson.spaces2}")
      val decoded = f.value.asJson.as[PaymentFlag]
      logger.info(s"${f.value} => $decoded")
    }

    val aFlag: PaymentFlag = PaymentFlag.tfNoDirectRipple
    val json               = aFlag.asJson
    logger.debug("Flag: " + json.spaces2)

  }
}
