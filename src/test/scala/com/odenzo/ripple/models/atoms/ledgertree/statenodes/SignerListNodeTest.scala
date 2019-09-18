package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.{JsonObject, Decoder}
import io.circe.syntax._
import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.caterrors.ModelsLibError

class SignerListNodeTest extends CodecTesting {

  val jsonStr = """ {
                  |      "Flags" : 65536,
                  |      "LedgerEntryType" : "SignerList",
                  |      "OwnerNode" : "0000000000000000",
                  |      "PreviousTxnID" : "11C80277E7D1B6919A657E72EA8FA1D1F7081930EF8C9DEBF62ABEB330737AC6",
                  |      "PreviousTxnLgrSeq" : 141336,
                  |      "SignerEntries" : [
                  |        {
                  |          "SignerEntry" : {
                  |            "Account" : "rsztqq2m9LfJskJAk8CX15twh284LcAB8y",
                  |            "SignerWeight" : 1
                  |          }
                  |        },
                  |        {
                  |          "SignerEntry" : {
                  |            "Account" : "rfZxEoKbksnFQHV4HqSCdtWU2Tbk72U2gk",
                  |            "SignerWeight" : 1
                  |          }
                  |        },
                  |        {
                  |          "SignerEntry" : {
                  |            "Account" : "r9hQZzGo26ZB59huqxDT6DBwbVk6FssToe",
                  |            "SignerWeight" : 1
                  |          }
                  |        },
                  |        {
                  |          "SignerEntry" : {
                  |            "Account" : "rwyB6f2ZfFmCqMmnxBAWaJc8URzWJv9ktX",
                  |            "SignerWeight" : 1
                  |          }
                  |        },
                  |        {
                  |          "SignerEntry" : {
                  |            "Account" : "rGcFwUAo9y8zV2wfimqAnfNAPWQUgXKER3",
                  |            "SignerWeight" : 1
                  |          }
                  |        }
                  |      ],
                  |      "SignerListID" : 0,
                  |      "SignerQuorum" : 5,
                  |      "index" : "00BA02629B50C964D6481C23121058A80B55AA496FA5C6333D49FB836E72E19B"
                  |    }""".stripMargin

  test("Decoding with new unwrapper") {

    val done = for {
      jobj <- parseAsJObj(jsonStr)
      obj  <- decodeObj(jobj)(Decoder[SignerListNode])
      back = obj.asJson
      _    = logger.debug(s"BACK JSON: ${back.spaces4}")
    } yield obj
    logIfError(done)
    logger.info(s"Obj: $done")

  }

}
