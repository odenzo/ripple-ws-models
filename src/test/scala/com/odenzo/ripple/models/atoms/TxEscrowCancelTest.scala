package com.odenzo.ripple.models.atoms

import com.odenzo.ripple.models.testkit.CodecTesting

class TxEscrowCancelTest extends CodecTesting {

  val data =
    """{
          "Account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
          "Fee" : "666",
          "Flags" : 2147483648,
          "LastLedgerSequence" : 4294967295,
          "Memos" : [
            {
              "Memo" : {
                "MemoData" : "44656661756C74204D656D6F",
                "MemoFormat" : "746578742F706C61696E3B636861727365743D5554462D38"
              }
            }
          ],
          "OfferSequence" : 4,
          "Owner" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
          "Sequence" : 4,
          "SigningPubKey" : "0330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD020",
          "TransactionType" : "EscrowCancel",
          "TxnSignature" : "304402207B6159EA8D0B533C2D5D00A61354C9E85D4538B1399F3DB6438603A214E850C502207E012E1D8DBE5B67B0C46E8CFE6ED21E43F14819915F54E26F5093E8BA22A5B8",
          "date" : 551425271,
          "hash" : "110AE1929997807BE443BBB25D4DA0717D304F7FDB243A5D9801F6DA9FB8645F",
          "inLedger" : 26026,
          "ledger_index" : 26026
        }"""
}
