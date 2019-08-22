package com.odenzo.ripple.models.atoms

import com.odenzo.ripple.models.testkit.CodecTesting

class RippleTime$Test extends CodecTesting {

  test("Round Tripping RippleTime") {

    // These should all be computed in well less than one second
    val now   = RippleTime.now()
    val again = RippleTime.fromInstant(now.asInstant)
    logger.debug(s"Now: $now \nAgain: $again")
    now shouldEqual again

  }
}
