package com.odenzo.ripple.models.atoms

import io.circe.{Encoder, Decoder, Json}
import io.circe.syntax._

import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.CirceCodecUtils

class SignersTest extends CodecTesting {

  val json =
    """
      |  [
      |                        {
      |                            "Signer" : {
      |                                "Account" : "rf6mJiffBZib3ERqwWJSFXpJh2LKBT8qZJ",
      |                                "SigningPubKey" : "03448D9DD30CB71D2F241B20B520ADBF4B4009FE6734FC0E96DCAAA582E125B004",
      |                                "TxnSignature" : "304402205D96EFADE921BC4BD36102704C2AA4C21EFD270738C0486C9CFF13B3B1D85C7A022003A545ACDC6CF82411C7C05AD561A3ECDE6381044180EE8D18654CBBF0C7CB9A"
      |                            }
      |                        },
      |                        {
      |                            "Signer" : {
      |                                "Account" : "rLw5cYtra2cxpBU2sxe4BbrAwWhxo4fYXb",
      |                                "SigningPubKey" : "02FB221FA02E0BD0A64BD8074787AC3ACEE389DD388D5D69260C1B0E651DFD841F",
      |                                "TxnSignature" : "304402206ABEFFA04485E29F6F7E25C9A46EAB1F390B01867ECCAA8167856E13EE02E5EE0220314BB13029218479AFFE6964AB80E70529B06A9EC3ABB368EAC15A6EA48620BE"
      |                            }
      |                        }
      |                    ]
      |""".stripMargin

  test("Codec") {
    val test = jsonRoundTrip(json)(Encoder[Signers], Decoder[Signers])
    testCompleted(test)
  }

  test("Empty String PubKey") {
    val foo = """ "" """
    val res = testCompleted(jsonRoundTrip[RipplePublicKey](foo))
    logger.debug(s"$res")
    logger.debug("BACK: " + res._2.asJson)
  }
}
