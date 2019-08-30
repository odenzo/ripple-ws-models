package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe.{Decoder, Encoder}

import com.odenzo.ripple.models.testkit.CodecTesting

class AccountInfoRqTest extends CodecTesting {

  val rsJson = """  {
                 |    "account_data" : {
                 |      "Account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
                 |      "Balance" : "99941997120000000",
                 |      "Flags" : 0,
                 |      "LedgerEntryType" : "AccountRoot",
                 |      "OwnerCount" : 0,
                 |      "PreviousTxnID" : "2905B5ABCEBCA656BA3CDDE4F60533528411E68757C8E6CF71AD0823290DC5FE",
                 |      "PreviousTxnLgrSeq" : 123,
                 |      "Sequence" : 59,
                 |      "index" : "2B6AC232AA4C4BE41BF49D2459FA4A0347E1B543A4C92FCEE0821C0201E2E9A8"
                 |    },
                 |    "ledger_current_index" : 126,
                 |    "validated" : false
                 | }
                 | """.stripMargin

  test("Decoding") {
    val obj = jsonRoundTrip[AccountInfoRs](rsJson)
    logger.debug(s"Object: $obj")
  }
}
