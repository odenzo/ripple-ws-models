package com.odenzo.ripple.models.atoms

import io.circe.{Json, Encoder, Decoder}

import com.odenzo.ripple.models.testkit.CodecTesting

/**
  * Only oddity on domain of Strings and JsonNumbers is an id of "" is accepted.
  */
class RippleMsgIdTest extends CodecTesting {

  test("Decoder String") {
    testDecoding(Json.fromString("blahblahblah"), Decoder[RippleMsgId]) shouldEqual RippleMsgId("blahblahblah")
  }

  test("Decoder Number") {
    testDecoding(Json.fromLong(1020303), Decoder[RippleMsgId]) shouldEqual RippleMsgId("1020303")
  }

  test("Decoder Boolean") {                                                       // While technically allowed I guess, we only deal with numbers and strings as id
    Decoder[RippleMsgId].decodeJson(Json.fromBoolean(false)).isLeft shouldBe true // Should fail
  }

  test("Decoder for No Field") { // Simulated by Json.Null too
    Decoder[RippleMsgId].decodeJson(Json.Null).isLeft shouldBe true
    parse(inFieldQuoted("not", "there")).as[RippleMsgId].isLeft shouldBe true
  }

  test("Encode") {
    val id = "testme"
    testEncoding(RippleMsgId("testme"), Encoder[RippleMsgId]) shouldEqual Json.fromString(id)
  }

  test("RoundTrip") {
    def round(v: RippleMsgId) = testRoundtrip(v, Encoder[RippleMsgId], Decoder[RippleMsgId])
    val properties            = "blahblahblah" :: "" :: "123" :: Nil
    properties.foreach(v => round(RippleMsgId(v)))

  }

  def asQuotedValue(v: String)                = s""" "$v" """ //Json.fromString(v).spaces2
  def inFieldQuoted(field: String, v: String) = s"""{ "$field":"$v" } """
  def inField(field: String, v: String)       = s"""{ "$field":$v } """
}

object RippleMsgIdTest {}
