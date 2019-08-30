package com.odenzo.ripple.models.atoms.ledgertree

import io.circe.{Decoder, Json}

import com.odenzo.ripple.models.testkit.CodecTesting

class AffectedLedgerNodeTest extends CodecTesting {

  test("Modified Node") {
    val res = Samples.json_1.as[LedgerNodeDelta]
    logger.info(s"Result: $res")
    testCompleted(res)
  }

  test("Another") {
    val json                      = parse(Samples.nodesFromList)
    val modifiedVal: Option[Json] = json.asObject.flatMap(o => o.apply("ModifiedNode"))
    val res                       = modifiedVal.map(_.as[LedgerNodeDelta])
    logger.info(s"Decoding just ModifiedNode: $res\n\n\n")

    val answer = Decoder[AffectedLedgerNode].decodeJson(json)
    logger.info(s"Anser: $answer")
  }
  object Samples {

    // Notes this has a wrapper on top.
    val nodesFromList = """{
                          |        "ModifiedNode": {
                          |          "FinalFields": {
                          |            "Account": "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
                          |            "Balance": "99979071222999940",
                          |            "Flags": 0,
                          |            "OwnerCount": 0,
                          |            "Sequence": 7
                          |          },
                          |          "LedgerEntryType": "AccountRoot",
                          |          "LedgerIndex": "2B6AC232AA4C4BE41BF49D2459FA4A0347E1B543A4C92FCEE0821C0201E2E9A8",
                          |          "PreviousFields": {
                          |            "Balance": "99979404555999950",
                          |            "Sequence": 6
                          |          },
                          |          "PreviousTxnID": "D17FB182804933CB23CB46E2275AA6F35AD8CBF0A812A34FA818BB24CC78E71A",
                          |          "PreviousTxnLgrSeq": 231
                          |        }
                          |      }""".stripMargin

    private val modifiedRippleState: String =
      """{
  "FinalFields": {
    "Balance": {
      "currency": "THB",
      "issuer": "rrrrrrrrrrrrrrrrrrrrBZbvji",
      "value": "378731.44"
    },
    "Flags": 65536,
    "HighLimit": {
      "currency": "THB",
      "issuer": "r9jYqY2NvMQXi35PfUtXHbCFgaVpSo1pxQ",
      "value": "0"
    },
    "HighNode": "0000000000000000",
    "LowLimit": {
      "currency": "THB",
      "issuer": "rsHxGxABfabqgECSpQULhf59XU959AGNho",
      "value": "720000"
    },
    "LowNode": "0000000000000000"
  },
  "LedgerEntryType": "RippleState",
  "LedgerIndex": "34D9796BF4589CF64583186CC4C68CB69F55144BF73A2C6E21CA66425D80DBCF",
  "PreviousFields": {
    "Balance": {
      "currency": "THB",
      "issuer": "rrrrrrrrrrrrrrrrrrrrBZbvji",
      "value": "378371.22"
    }
  },
  "PreviousTxnID": "37C5CBF3F0020CC0733EEEEF4905D30D171685DAD013CC65F5D22CCF91256DBB",
  "PreviousTxnLgrSeq": 48372
}
      """

    val json_1: Json = parse(modifiedRippleState)

  }
}
