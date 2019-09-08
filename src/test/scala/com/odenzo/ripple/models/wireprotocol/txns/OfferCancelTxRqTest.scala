package com.odenzo.ripple.models.wireprotocol.txns

import com.odenzo.ripple.models.testkit.CodecTesting

class OfferCancelTxRqTest extends CodecTesting {

  private object Data {

    val rq =
      """ {
  "command" : "sign",
  "tx_json" : {
    "TransactionType" : "OfferCancel",
    "Account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
    "OfferSequence" : 2,
    "Memos" : [
      {
        "Memo" : {
          "MemoData" : "44656661756C74204D656D6F",
          "MemoFormat" : "746578742F706C61696E3B636861727365743D5554462D38"
        }
      }
    ],
    "Sequence" : null,
    "LastLedgerSequence" : 4294967295,
    "Fee" : "666",
    "Flags" : null
  },
  "secret" : "snoPBrXtMeMyMHUVTgbuqAfg1SUTb",
  "offline" : false,
  "build_path" : null,
  "fee_multi_max" : 1000,
  "fee_div_max" : null,
  "id" : "a85f21d6-49ac-4e00-a865-87b1ca9360dd"
}"""

  }

}
