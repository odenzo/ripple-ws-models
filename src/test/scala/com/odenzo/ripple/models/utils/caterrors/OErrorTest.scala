package com.odenzo.ripple.models.utils.caterrors

import cats.implicits._
import io.circe.Json

import com.odenzo.ripple.models.testkit.CodecTesting

class OErrorTest extends CodecTesting {
  import ModelsLibError._

  test("Message with Message") {
    val root = OError("I stubed my toe")
    val top  = OError("While Trying to Save the World", root)

    val msg = top.show
    logger.info(s"M & M ${msg}")
  }

  test("Aliasing") {
    val msg = OError("This is a message").show
    logger.info(s"[$msg]")
  }

  test("JSon on Top") {
    val json =
      AppJsonError("This is a fake OErrorJson", Json.fromString("hello this is json really!"), OError("The Root"))
    val str = json.show
    logger.info(s"JSONErrpr: [$str]")
  }

  test("JSon on Top As OError") {
    val json =
      AppJsonError("This is a fake OErrorJson", Json.fromString("hello this is json really!"), OError("The Root"))
    val upped: ModelsLibError = json
    val str                   = upped.show
    logger.info(s"JSONErrpr: [$str]")
  }

  test("JSon on Top As BaseError") {
    val json =
      AppJsonError("This is a fake OErrorJson", Json.fromString("hello this is json really!"), OError("The Root"))
    val upped: ModelsLibError = json
    val str                   = upped.show
    logger.info(s"JSONErrpr: [$str]")
  }

}
