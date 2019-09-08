package com.odenzo.ripple.models.atoms

import io.circe.Decoder.Result
import io.circe.syntax._
import io.circe.{Json, Encoder}

import com.odenzo.ripple.models.testkit.CodecTesting

class CurrencyAmount$Test extends CodecTesting {

  test("Encoding Drops") {

    testEncoding(Drops(50), Encoder[Drops])
    testEncoding(Drops(50), Encoder[CurrencyAmount])
    val amt: CurrencyAmount = Drops(55)
    amt.asJson shouldEqual Json.fromString("55")

  }

  test("Test Conversion") {
    Drops(1) shouldEqual Drops.fromXrp("0.000001")
  }

  test("Encoding FiatAmount") {
    val amount: FiatAmount         = FiatAmount(BigDecimal(666), Script(Currency.NZD, AccountAddr("rGarbage")))
    val fiatAmount: CurrencyAmount = amount: CurrencyAmount
    testEncoding(amount, Encoder[CurrencyAmount])
    testEncoding(amount, Encoder[FiatAmount])
    testEncoding(fiatAmount, Encoder[CurrencyAmount])
    logger.info("Seeing if the pulled in encoder goes down to subclass directly")
    amount.asJson
    fiatAmount.asJson

  }

  test("Decoding Drops") {
    val drops: Result[Drops] = Json.fromString("66").as[Drops]
    logger.info(s"Simple Drops $drops")
    testCompleted(drops) shouldEqual Drops(66)

    val dropsCA: Result[CurrencyAmount] = Json.fromString("66").as[CurrencyAmount]
    logger.info(s"Drops CAmount $dropsCA")
    testCompleted(dropsCA) shouldEqual Drops(66)

  }

  test("Decoding Fiat") {
    val fiatObj = FiatAmount(BigDecimal(666), Script(Currency.NZD, AccountAddr("rGarbage")))
    val sample  = """{
                   |  "value" : 666,
                   |  "currency" : "NZD",
                   |  "issuer" : "rGarbage"
                   |}""".stripMargin

    val json: Json = parse(sample)
    val fiat       = json.as[FiatAmount]
    testCompleted(fiat) shouldEqual fiatObj
    val currencyAmount: Result[CurrencyAmount] = json.as[CurrencyAmount]
    testCompleted(currencyAmount) shouldEqual fiatObj

  }
}
