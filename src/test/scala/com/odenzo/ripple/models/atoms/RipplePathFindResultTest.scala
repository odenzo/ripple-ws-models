package com.odenzo.ripple.models.atoms

import io.circe.{Json, Decoder}

import com.odenzo.ripple.models.testkit.CodecTesting
import io.circe.optics._
import monocle.Optional
import cats._
import cats.data._
import cats.implicits._
import cats.syntax.traverse

import com.odenzo.ripple.models.utils.caterrors.{ModelsLibError, OError, AppJsonDecodingError}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.pathandorderbook.RipplePathFindRs

class RipplePathFindResultTest extends CodecTesting {

  val resStr =
    """
      |{

      |        "alternatives" : [
      |            {
      |                "paths_canonical" : [
      |                ],
      |                "paths_computed" : [
      |                    [
      |                        {
      |                            "account" : "rLGPpk8iBy31NC27mrnE7GBtmLhjcdwNUx",
      |                            "type" : 1,
      |                            "type_hex" : "0000000000000001"
      |                        },
      |                        {
      |                            "currency" : "NZD",
      |                            "issuer" : "r9xPT1GZVobG8EQKVYx6g9cdYV7qsizdhF",
      |                            "type" : 48,
      |                            "type_hex" : "0000000000000030"
      |                        }
      |                    ]
      |                ],
      |                "source_amount" : {
      |                    "currency" : "USD",
      |                    "issuer" : "r3sukQoNg7CPupp1c6u3eFAFSs7NmvVB7a",
      |                    "value" : "6.666666666666667"
      |                }
      |            }
      |        ],
      |        "destination_account" : "r4gXfxEVSM8nhTwb7xicJCohNokUbRdCnY",
      |        "destination_amount" : {
      |            "currency" : "NZD",
      |            "issuer" : "r9xPT1GZVobG8EQKVYx6g9cdYV7qsizdhF",
      |            "value" : "10"
      |        },
      |        "destination_currencies" : [
      |            "NZD",
      |            "XRP"
      |        ],
      |        "full_reply" : true,
      |        "id" : "<No MsgId>",
      |        "ledger_current_index" : 53,
      |        "source_account" : "r3sukQoNg7CPupp1c6u3eFAFSs7NmvVB7a",
      |        "validated" : false
      |    }
      |""".stripMargin

  test("Paths") {
    val obj: RipplePathFindRs = testCompleted(parseAndDecode(resStr, Decoder[RipplePathFindRs]))
    logger.debug(s"OBJ: ${pprint.apply(obj)}")
  }
}
