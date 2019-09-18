package com.odenzo.ripple.models.atoms

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.caterrors.ModelsLibError

class PaymentPathTest extends CodecTesting {

  val jsonStr = """ [
                  |    [
                  |      {
                  |        "currency" : "CNY",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "XLM",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "XRP",
                  |        "type" : 16,
                  |        "type_hex" : "0000000000000010"
                  |      },
                  |      {
                  |        "currency" : "ZCN",
                  |        "issuer" : "r8HgVGenRTAiNSM5iqt9PX2D2EczFZhZr",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      }
                  |    ],
                  |    [
                  |      {
                  |        "currency" : "XLM",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "CNY",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "XRP",
                  |        "type" : 16,
                  |        "type_hex" : "0000000000000010"
                  |      },
                  |      {
                  |        "currency" : "ZCN",
                  |        "issuer" : "r8HgVGenRTAiNSM5iqt9PX2D2EczFZhZr",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      }
                  |    ],
                  |    [
                  |      {
                  |        "currency" : "CNY",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "USD",
                  |        "issuer" : "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "XRP",
                  |        "type" : 16,
                  |        "type_hex" : "0000000000000010"
                  |      },
                  |      {
                  |        "currency" : "ZCN",
                  |        "issuer" : "r8HgVGenRTAiNSM5iqt9PX2D2EczFZhZr",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      }
                  |    ],
                  |    [
                  |      {
                  |        "currency" : "USD",
                  |        "issuer" : "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "CNY",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "XRP",
                  |        "type" : 16,
                  |        "type_hex" : "0000000000000010"
                  |      },
                  |      {
                  |        "currency" : "ZCN",
                  |        "issuer" : "r8HgVGenRTAiNSM5iqt9PX2D2EczFZhZr",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      }
                  |    ],
                  |    [
                  |      {
                  |        "currency" : "CNY",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "ULT",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "XRP",
                  |        "type" : 16,
                  |        "type_hex" : "0000000000000010"
                  |      },
                  |      {
                  |        "currency" : "ZCN",
                  |        "issuer" : "r8HgVGenRTAiNSM5iqt9PX2D2EczFZhZr",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      }
                  |    ],
                  |    [
                  |      {
                  |        "currency" : "ULT",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "CNY",
                  |        "issuer" : "rKiCet8SdvWxPXnAgYarFUXMh1zCPz432Y",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      },
                  |      {
                  |        "currency" : "XRP",
                  |        "type" : 16,
                  |        "type_hex" : "0000000000000010"
                  |      },
                  |      {
                  |        "currency" : "ZCN",
                  |        "issuer" : "r8HgVGenRTAiNSM5iqt9PX2D2EczFZhZr",
                  |        "type" : 48,
                  |        "type_hex" : "0000000000000030"
                  |      }
                  |    ]
                  |  ]""".stripMargin

  val json = parseAsJson(jsonStr)

  test("ListList") {
    val res: List[List[PaymentPathStep]] = testCompleted(json.flatMap(decode(_)(Decoder[List[List[PaymentPathStep]]])))
    logger.debug(s"${pprint.apply(res)}")
  }

  test("List") {
    val res: List[PaymentPath] = testCompleted(json.flatMap(decode(_)(Decoder[List[PaymentPath]])))
    logger.debug(s"${pprint.apply(res)}")
  }
}
