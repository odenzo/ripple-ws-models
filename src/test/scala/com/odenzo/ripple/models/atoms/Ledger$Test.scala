package com.odenzo.ripple.models.atoms

import io.circe.Decoder.Result
import io.circe._
import io.circe.syntax._

import com.odenzo.ripple.models.testkit.CodecTesting

class Ledger$Test extends CodecTesting {

  val index = LedgerSequence(666L)
  val name  = LedgerName("lname")
  val hash  = LedgerHash(RippleHash("SomeLongHash"))

  private def multLongJson = parse("""{ "ledger_index": 777, "ledger_hash": "hashashashashash"}    """)
  private def multNameJson = parse("""{ "ledger_index": "validated", "ledger_hash": "hashashashashash"}    """)
  private def longJson     = parse("""{ "ledger_index": 777   } """)
  private def nameJson     = parse("""{ "ledger_index": "validated" }   """)
  private def hashJson     = parse("""{ "ledger_hash": "hashashashashash"}    """)

  test("Encode LedgerId ") {
    val t: LedgerIndex = index

    testEncoding(index: LedgerIndex, Encoder[LedgerIndex]) shouldEqual Json.fromLong(index.v)
    testEncoding(name: LedgerIndex, Encoder[LedgerIndex]) shouldEqual Json.fromString(name.v)

    testEncoding(name: LedgerIndex, Encoder[LedgerIndex]) shouldEqual (name: LedgerIndex).asJson
    testEncoding(t, Encoder[LedgerIndex]) shouldEqual t.asJson
    testEncoding(index, Encoder[LedgerSequence]) shouldEqual index.asJson

  }

  /** Behaviour of the decoders for inspection. */
  test("Exploratory Decoding") {
    decodingScenarioa(multLongJson)
    decodingScenarioa(multNameJson)
    decodingScenarioa(longJson)
    decodingScenarioa(nameJson)
    decodingScenarioa(hashJson)
  }

  private def decodingScenarioa(json: Json) = {

    val deepIndex: Result[LedgerSequence] = json.hcursor.get[LedgerSequence]("ledger_index")
    val deepName: Result[LedgerName]      = json.hcursor.get[LedgerName]("ledger_index")
    val shallow: Result[LedgerIndex]      = json.hcursor.get[LedgerIndex]("ledger_index")
    val deepHash: Result[LedgerHash]      = json.hcursor.get[LedgerHash]("ledger_hash")
    def full                              = Decoder[Ledger].decodeJson(json)

    logger.debug(s"Decoding Scenarios on: ${json.spaces2}")
    logResult(deepIndex, "Deep Index")
    logResult(deepName, "Deep Name")
    logResult(shallow, "Shallow LedgerId")
    logResult(deepHash, "Deep Hash")
    logResult(full, "Full Ledger")
  }
}
