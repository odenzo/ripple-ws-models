package com.odenzo.ripple.models.atoms.ledgertree.nodes

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting

class AccountRootNodeTest extends CodecTesting {

  private val decoder = Decoder[AccountRootNode]

  test("Decoding") {
    testDecoding(nodesFromList, decoder)
  }
  val nodesFromList =
    """{
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

}
