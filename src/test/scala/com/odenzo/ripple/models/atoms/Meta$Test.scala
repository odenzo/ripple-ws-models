package com.odenzo.ripple.models.atoms

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting

class Meta$Test extends CodecTesting {

  test("Bootstrap Early Rs") {
    val ans: Meta = testDecoding(Samples.another, Decoder[Meta])
    logger.info(s"Meta: $ans")
  }

  object Samples {

    val another = """{
    "AffectedNodes" : [
      {
        "ModifiedNode" : {
          "FinalFields" : {
            "Account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
            "Balance" : "99958999999999950",
            "Flags" : 0,
            "OwnerCount" : 0,
            "Sequence" : 6
          },
          "LedgerEntryType" : "AccountRoot",
          "LedgerIndex" : "2B6AC232AA4C4BE41BF49D2459FA4A0347E1B543A4C92FCEE0821C0201E2E9A8",
          "PreviousFields" : {
            "Balance" : "99959999999999960",
            "Sequence" : 5
          },
          "PreviousTxnID" : "82610708AAFFF74D2AC25684838D4BFDBEFE76FA54D3A83317E881E3E2662869",
          "PreviousTxnLgrSeq" : 36230
        }
      },
      {
        "ModifiedNode" : {
          "FinalFields" : {
            "Account" : "rHRCVD1zpGzCmt3UPHgc2FoS2622KQUZT3",
            "Balance" : "10999999999980",
            "Flags" : 8388608,
            "OwnerCount" : 0,
            "RegularKey" : "rJBhjdNqAFJpNUeNK5fDsxiATMgnCeuZ45",
            "Sequence" : 3
          },
          "LedgerEntryType" : "AccountRoot",
          "LedgerIndex" : "6EB0F29A4637F741A13B11C00106904D856C6A32E911ADFA27086B59D92BEF6E",
          "PreviousFields" : {
            "Balance" : "9999999999980",
            "Flags" : 8454144
          },
          "PreviousTxnID" : "0E0B17E124E6CEE5FD7FD7B9AE9FCDD115E52431748D313B451FC5FB80540EB2",
          "PreviousTxnLgrSeq" : 36231
        }
      }
    ],
    "TransactionIndex" : 2,
    "TransactionResult" : "tesSUCCESS",
    "delivered_amount" : "1000000000000"
  }"""

    /**
      * The account_tx returns this for each transaction. The nice thing is that it has all accounts
      * and objects effected by a transaction, including previous balance.
      * We focus on just the interesting business level parts for now.
      * For each transaction we have (from tx) the sender and receiver for the actual payment.
      * rrrrrrrrrrrrrrrrrrrrBZbvji the XRP Genesis account or black hole account, I forget.
      *
      *
      */
    val s =
      """{
        "meta" : {
          "AffectedNodes" : [
            {
              "ModifiedNode" : {
                "FinalFields" : {
                  "Account" : "r6EtuyVw1XWc11dswUb6ELc5pRq6MkjZd",
                  "Balance" : "7009999420",
                  "Flags" : 9502720,
                  "OwnerCount" : 1,
                  "RegularKey" : "rfgeHVA9kEBkCC9fW6irBCs8yN5v9MVPwU",
                  "Sequence" : 59
                },
                "LedgerEntryType" : "AccountRoot",
                "LedgerIndex" : "18A3E06F49555C941ED8B5CBDE42D49CF338F669FFE84274D4540B61768A4BD7",
                "PreviousFields" : {
                  "Balance" : "7009999430",
                  "Sequence" : 58
                },
                "PreviousTxnID" : "37C5CBF3F0020CC0733EEEEF4905D30D171685DAD013CC65F5D22CCF91256DBB",
                "PreviousTxnLgrSeq" : 48372
              }
            },
            {
              "ModifiedNode" : {
                "FinalFields" : {
                  "Balance" : {
                    "currency" : "THB",
                    "issuer" : "rrrrrrrrrrrrrrrrrrrrBZbvji",
                    "value" : "378731.44"
                  },
                  "Flags" : 65536,
                  "HighLimit" : {
                    "currency" : "THB",
                    "issuer" : "r9jYqY2NvMQXi35PfUtXHbCFgaVpSo1pxQ",
                    "value" : "0"
                  },
                  "HighNode" : "0000000000000000",
                  "LowLimit" : {
                    "currency" : "THB",
                    "issuer" : "rsHxGxABfabqgECSpQULhf59XU959AGNho",
                    "value" : "720000"
                  },
                  "LowNode" : "0000000000000000"
                },
                "LedgerEntryType" : "RippleState",
                "LedgerIndex" : "34D9796BF4589CF64583186CC4C68CB69F55144BF73A2C6E21CA66425D80DBCF",
                "PreviousFields" : {
                  "Balance" : {
                    "currency" : "THB",
                    "issuer" : "rrrrrrrrrrrrrrrrrrrrBZbvji",
                    "value" : "378371.22"
                  }
                },
                "PreviousTxnID" : "37C5CBF3F0020CC0733EEEEF4905D30D171685DAD013CC65F5D22CCF91256DBB",
                "PreviousTxnLgrSeq" : 48372
              }
            },
            {
              "ModifiedNode" : {
                "FinalFields" : {
                  "Balance" : {
                    "currency" : "THB",
                    "issuer" : "rrrrrrrrrrrrrrrrrrrrBZbvji",
                    "value" : "341268.56"
                  },
                  "Flags" : 65536,
                  "HighLimit" : {
                    "currency" : "THB",
                    "issuer" : "r9jYqY2NvMQXi35PfUtXHbCFgaVpSo1pxQ",
                    "value" : "0"
                  },
                  "HighNode" : "0000000000000000",
                  "LowLimit" : {
                    "currency" : "THB",
                    "issuer" : "r6EtuyVw1XWc11dswUb6ELc5pRq6MkjZd",
                    "value" : "720000"
                  },
                  "LowNode" : "0000000000000000"
                },
                "LedgerEntryType" : "RippleState",
                "LedgerIndex" : "42E37BD9FC79329822FD7A6EC6A24253B9B4E59175D7F8FE341BDA3919A31371",
                "PreviousFields" : {
                  "Balance" : {
                    "currency" : "THB",
                    "issuer" : "rrrrrrrrrrrrrrrrrrrrBZbvji",
                    "value" : "341628.78"
                  }
                },
                "PreviousTxnID" : "37C5CBF3F0020CC0733EEEEF4905D30D171685DAD013CC65F5D22CCF91256DBB",
                "PreviousTxnLgrSeq" : 48372
              }
            }
          ],
          "TransactionIndex" : 0,
          "TransactionResult" : "tesSUCCESS",
          "delivered_amount" : {
            "currency" : "THB",
            "issuer" : "r9jYqY2NvMQXi35PfUtXHbCFgaVpSo1pxQ",
            "value" : "360.22"
          }
        },
        "tx" : {
          "Account" : "r6EtuyVw1XWc11dswUb6ELc5pRq6MkjZd",
          "Amount" : {
            "currency" : "THB",
            "issuer" : "r9jYqY2NvMQXi35PfUtXHbCFgaVpSo1pxQ",
            "value" : "360.22"
          },
          "Destination" : "rsHxGxABfabqgECSpQULhf59XU959AGNho",
          "Fee" : "10",
          "Flags" : 131072,
          "LastLedgerSequence" : 48382,
          "Sequence" : 58,
          "SigningPubKey" : "03577FFF40E68101E46C6F7D99EC0AFCFB52EC23BE99A5FD65A5FD97538A07AA38",
          "TransactionType" : "Payment",
          "TxnSignature" : "3045022100D8F2CE228E3874112BD5FA01ECEFF61BA3FC7B7023B9F25373669E70F5CBA29F02206C4F6A2F24B060844BDBE8BC93B009FF95BDB19DF0915FB089C9589EC10725C4",
          "date" : 540828891,
          "hash" : "1E09160EE36495F09E292D5262108B37F478DB3F9A0B2401AB418ACC49144061",
          "inLedger" : 48373,
          "ledger_index" : 48373
        },
        "validated" : true
      }
  """

    /*
       {
      "id" : "84542138-eef2-4363-a51d-13743d6e0625",
      "command" : "account_tx",
      "account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
      "ledger_index_min" : -1,
      "ledger_index_max" : -1,
      "limit" : 10
    }
    :I: 10:57.953 [com.odenzo.ripple.core.actors.LedgerMonitor$] [rippled-actors-akka.actor.default-dispatcher-3]
    ACTOR:[] LedgerMonitor.scala:79- WebSocket Opened?: ValidUpgrade(HttpResponse(101 Switching Protocols,List(Upgrade:
    websocket, Sec-WebSocket-Accept: UxKuS7HV0ObWamiEBPvxkonKxaM=, Server: rippled-0.50.2, Connection: upgrade),
    HttpEntity.Strict(none/none,ByteString()),HttpProtocol(HTTP/1.1)),None)
    :D: 10:57.954 [com.odenzo.ripple.core.actors.LedgerMonitorActor] [rippled-actors-akka.actor.default-dispatcher-4]
    ACTOR:[] LedgerMonitorActor.scala:114- Startup OK!
    :I: 10:58.008 [com.odenzo.ripple.core.coms.OneOffWebSocket$] [default-akka.actor.default-dispatcher-4] ACTOR:[]
    OneOffWebSocket.scala:86- 1-1 Inbound Success Response:
    {
      "id" : "84542138-eef2-4363-a51d-13743d6e0625",
      "result" : {
        "account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
        "ledger_index_max" : 14561,
        "ledger_index_min" : 1,
        "limit" : 10,
        "marker" : {
          "ledger" : 12019,
          "seq" : 0
        },
        "transactions" : [
          {
            "meta" : {
              "AffectedNodes" : [
                {
                  "CreatedNode" : {
                    "LedgerEntryType" : "AccountRoot",
                    "LedgerIndex" : "02051184B0B88ECE9E5B202B67FEB8F603DB77518995EDD42DF9BEFD722352A0",
                    "NewFields" : {
                      "Account" : "rhrYccveaHJisNbr8QtTSgJUgQK4RZTUJs",
                      "Balance" : "9999990000000",
                      "Sequence" : 1
                    }
                  }
                },
                {
                  "ModifiedNode" : {
                    "FinalFields" : {
                      "Account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
                      "Balance" : "99810000039999810",
                      "Flags" : 0,
                      "OwnerCount" : 0,
                      "Sequence" : 20
                    },
                    "LedgerEntryType" : "AccountRoot",
                    "LedgerIndex" : "2B6AC232AA4C4BE41BF49D2459FA4A0347E1B543A4C92FCEE0821C0201E2E9A8",
                    "PreviousFields" : {
                      "Balance" : "99820000029999820",
                      "Sequence" : 19
                    },
                    "PreviousTxnID" : "C77CF2223A7EE24FAB5F15A1D2F4EA444B23B6782201D56E1DACD4B09268BCD8",
                    "PreviousTxnLgrSeq" : 13282
                  }
                }
              ],
              "TransactionIndex" : 0,
              "TransactionResult" : "tesSUCCESS",
              "delivered_amount" : "9999990000000"
            },
            "tx" : {
              "Account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
              "Amount" : "9999990000000",
              "Destination" : "rhrYccveaHJisNbr8QtTSgJUgQK4RZTUJs",
              "Fee" : "10",
              "Flags" : 2147483648,
              "LastLedgerSequence" : 13292,
              "Memos" : [
                {
                  "Memo" : {
                    "MemoData" : "496E697469616C2046756E64696E6720206F66206E6577206163636F756E74",
                    "MemoFormat" : "746578742F706C61696E3B636861727365743D5554462D38"
                  }
                }
              ],
              "Sequence" : 19,
              "SigningPubKey" : "0330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD020",
              "TransactionType" : "Payment",
              "TxnSignature" :
              "30450221009183753CDE3A636B665A3EB75382CDE31C91662FC90E52248E62CA0AC3EF8C13022074F80F2E9B7C7EE4529F3BB26C69F021A078BFE924311E1B705CE78231B334DA",
              "date" : 541061220,
              "hash" : "B8A9A828064FC7A768D6761570D5F882D3023D4DC06B37EDC7F8ED184A58E146",
              "inLedger" : 13283,
              "ledger_index" : 13283
            },
            "validated" : true
          },
   */
  }
}
