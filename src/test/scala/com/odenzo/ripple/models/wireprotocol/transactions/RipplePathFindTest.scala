package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.CirceUtils

class RipplePathFindTest extends CodecTesting {

  test("Response Decoding") {
    val rsText = """{
                   |        "alternatives": [
                   |            {
                   |                "paths_canonical": [],
                   |                "paths_computed": [
                   |                    [
                   |                        {
                   |                            "currency": "USD",
                   |                            "issuer": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                   |                            "type": 48,
                   |                            "type_hex": "0000000000000030"
                   |                        },
                   |                        {
                   |                            "account": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                   |                            "type": 1,
                   |                            "type_hex": "0000000000000001"
                   |                        }
                   |                    ],
                   |                    [
                   |                        {
                   |                            "currency": "USD",
                   |                            "issuer": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                   |                            "type": 48,
                   |                            "type_hex": "0000000000000030"
                   |                        },
                   |                        {
                   |                            "account": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                   |                            "type": 1,
                   |                            "type_hex": "0000000000000001"
                   |                        },
                   |                        {
                   |                            "account": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                   |                            "type": 1,
                   |                            "type_hex": "0000000000000001"
                   |                        }
                   |                    ],
                   |                    [
                   |                        {
                   |                            "currency": "USD",
                   |                            "issuer": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                   |                            "type": 48,
                   |                            "type_hex": "0000000000000030"
                   |                        },
                   |                        {
                   |                            "account": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                   |                            "type": 1,
                   |                            "type_hex": "0000000000000001"
                   |                        },
                   |                        {
                   |                            "account": "rLpq4LgabRfm1xEX5dpWfJovYBH6g7z99q",
                   |                            "type": 1,
                   |                            "type_hex": "0000000000000001"
                   |                        },
                   |                        {
                   |                            "account": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                   |                            "type": 1,
                   |                            "type_hex": "0000000000000001"
                   |                        }
                   |                    ],
                   |                    [
                   |                        {
                   |                            "currency": "USD",
                   |                            "issuer": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                   |                            "type": 48,
                   |                            "type_hex": "0000000000000030"
                   |                        },
                   |                        {
                   |                            "account": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                   |                            "type": 1,
                   |                            "type_hex": "0000000000000001"
                   |                        },
                   |                        {
                   |                            "account": "rPuBoajMjFoDjweJBrtZEBwUMkyruxpwwV",
                   |                            "type": 1,
                   |                            "type_hex": "0000000000000001"
                   |                        },
                   |                        {
                   |                            "account": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                   |                            "type": 1,
                   |                            "type_hex": "0000000000000001"
                   |                        }
                   |                    ]
                   |                ],
                   |                "source_amount": "256987"
                   |            }
                   |        ],
                   |        "destination_account": "r9cZA1mLK5R5Am25ArfXFmqgNwjZgnfk59",
                   |        "destination_currencies": [
                   |            "015841551A748AD2C1F76FF6ECB0CCCD00000000",
                   |            "JOE",
                   |            "DYM",
                   |            "EUR",
                   |            "CNY",
                   |            "MXN",
                   |            "BTC",
                   |            "USD",
                   |            "XRP"
                   |        ]
                   |    
                   |}""".stripMargin

    val rs = CirceUtils.parseAndDecode(rsText, Decoder[RipplePathFindRs])
    val ok = testCompleted(rs)
    logger.info(s"OK = $ok")
  }
}
