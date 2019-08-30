package com.odenzo.ripple.models.atoms

import com.odenzo.ripple.models.testkit.CodecTesting

import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

class RippleQualityTest extends CodecTesting {

  // Test of unwrapping a nested Codec
  test("Quality") {

    val t    = RippleQuality(UInt32(123))
    val json = t.asJson
    val t2   = json.as[RippleQuality]
    testCompleted(t2)
  }
}
