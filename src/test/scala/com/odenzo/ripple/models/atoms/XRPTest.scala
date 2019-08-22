package com.odenzo.ripple.models.atoms

import cats.implicits._

import com.odenzo.ripple.models.testkit.CodecTesting

class XRPTest extends CodecTesting {

  test("Precision") {

    val big = Drops.fromXrp(1000)
    logger.info(s"Big Show: ${big.show}")
    logger.info(s"Big     : $big")
  }

  test("MAX Precision") {

    val big = Drops.fromXrp(Long.MaxValue)
    logger.info(s"Big Show: ${big.show}")
    logger.info(s"Big     : $big")
  }

}
