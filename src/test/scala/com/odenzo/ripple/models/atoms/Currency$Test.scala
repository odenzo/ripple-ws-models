package com.odenzo.ripple.models.atoms

import io.circe.{Decoder, Encoder}

import com.odenzo.ripple.models.support.GenesisAccount
import com.odenzo.ripple.models.testkit.CodecTesting

class Currency$Test extends CodecTesting {

  test("Script") {
    val obj  = Script(Currency.NZD, GenesisAccount.address)
    val json = parse("""
                       |{
                       |  "currency" : "NZD",
                       |  "issuer" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh"
                       |}
      """.stripMargin)

    testEncoding(obj, Encoder[Script]) shouldEqual json
    testDecoding(json, Decoder[Script]) shouldEqual obj
    testRoundtrip(obj, Encoder[Script], Decoder[Script])
  }

  test("Currency") {
    testRoundtrip(Currency.USD, Encoder[Currency], Decoder[Currency])
  }
}
