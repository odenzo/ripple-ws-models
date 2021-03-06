package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.pathandorderbook.RipplePathFindRs
import io.circe.literal._
class RipplePathFindTest extends CodecTesting {

  test("Response Decoding") {
    val rsText = json"""{
             "alternatives": [
                 {
                      "paths_canonical": [],
                     "paths_computed": [
                         [
                             {
                                 "currency": "USD",
                                 "issuer": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                                 "type": 48,
                                 "type_hex": "0000000000000030"
                             },
                             {
                                 "account": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                                 "type": 1,
                                 "type_hex": "0000000000000001"
                             }
                         ],
                         [
                             {
                                 "currency": "USD",
                                 "issuer": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                                 "type": 48,
                                 "type_hex": "0000000000000030"
                             },
                             {
                                 "account": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                                 "type": 1,
                                 "type_hex": "0000000000000001"
                             },
                             {
                                 "account": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                                 "type": 1,
                                 "type_hex": "0000000000000001"
                             }
                         ],
                         [
                             {
                                 "currency": "USD",
                                 "issuer": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                                 "type": 48,
                                 "type_hex": "0000000000000030"
                             },
                             {
                                 "account": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                                 "type": 1,
                                 "type_hex": "0000000000000001"
                             },
                             {
                                 "account": "rLpq4LgabRfm1xEX5dpWfJovYBH6g7z99q",
                                 "type": 1,
                                 "type_hex": "0000000000000001"
                             },
                             {
                                 "account": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                                 "type": 1,
                                 "type_hex": "0000000000000001"
                             }
                         ],
                         [
                             {
                                 "currency": "USD",
                                 "issuer": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                                 "type": 48,
                                 "type_hex": "0000000000000030"
                             },
                             {
                                 "account": "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
                                 "type": 1,
                                 "type_hex": "0000000000000001"
                             },
                             {
                                 "account": "rPuBoajMjFoDjweJBrtZEBwUMkyruxpwwV",
                                 "type": 1,
                                 "type_hex": "0000000000000001"
                             },
                             {
                                 "account": "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
                                 "type": 1,
                                 "type_hex": "0000000000000001"
                             }
                         ]
                     ],
                     "source_amount": "256987"
                 }
             ],
             "destination_account": "r9cZA1mLK5R5Am25ArfXFmqgNwjZgnfk59",
             "destination_currencies": [
                 "015841551A748AD2C1F76FF6ECB0CCCD00000000",
                 "JOE",
                 "DYM",
                 "EUR",
                 "CNY",
                 "MXN",
                 "BTC",
                 "USD",
                 "XRP"
             ],
            "source_account" : "r3sukQoNg7CPupp1c6u3eFAFSs7NmvVB7a"
     }"""

    // Not sure source account is optional or I cut it off by mistake
    val rs = CirceUtils.decode(rsText)(Decoder[RipplePathFindRs])
    val ok = testCompleted(rs)
    logger.info(s"OK = $ok")
  }
}
