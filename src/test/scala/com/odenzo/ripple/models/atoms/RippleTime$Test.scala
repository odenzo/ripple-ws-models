package com.odenzo.ripple.models.atoms

import com.odenzo.ripple.models.testkit.CodecTesting
import io.circe.syntax._
class RippleTime$Test extends CodecTesting {

  test("Round Tripping RippleTime") {

    // These should all be computed in well less than one second
    val now   = RippleTime.now()
    val again = RippleTime.fromInstant(now.asInstant)
    logger.debug(s"Now: $now \nAgain: $again")
    now shouldEqual again

  }

  test("Codec Annotation") {
    val t    = RippleTime(123123)
    val json = t.asJson
    logger.debug(s"JSON for $t is \n ${json.spaces4}")
    val t2 = json.as[RippleTime]
  }
}
