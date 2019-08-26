package com.odenzo.ripple.models.atoms

import com.odenzo.ripple.models.testkit.CodecTesting

class LedgerCurrentIndexTest extends CodecTesting {

  test("generic-extras") {
    import io.circe.syntax._
    val json = LedgerCurrentIndex(12345).asJson
    val obj  = json.as[LedgerCurrentIndex]
    logger.debug(s"JSON $json   $obj")
  }

}
