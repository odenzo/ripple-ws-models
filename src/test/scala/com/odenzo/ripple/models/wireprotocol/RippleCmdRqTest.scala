package com.odenzo.ripple.models.wireprotocol

import com.odenzo.ripple.models.testkit.CodecTesting
import io.circe.syntax._
import cats._
import cats.data._
import cats.implicits._

import com.odenzo.ripple.models.atoms.{Limit, Marker}

class RippleCmdRqTest extends CodecTesting {

  test("CommonCmdRq Codec") {
    val obj =
      CommonCmdRq(
        ledger_hash = dummyLedgerHash.some,
        limit = Limit(50).some,
        marker = Marker("MarkerValue".asJson).some
      )

    val jobj = obj.asJsonObject
    logger.debug(s"$obj => \n ${jobj.asJson.spaces4}")

    objRoundTrip(obj)

    def encode(a: CommonCmdRq): Unit = logger.debug(s"$a => \n ${a.asJsonObject.asJson.spaces4}")
  }

  test("CommonCmdRs Codec") {
    val obj = CommonCmdRs.empty
    objRoundTrip(obj)
  }
}
