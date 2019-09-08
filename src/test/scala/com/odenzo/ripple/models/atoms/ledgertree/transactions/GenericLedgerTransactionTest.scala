package com.odenzo.ripple.models.atoms.ledgertree.transactions

import io.circe.Decoder.Result
import io.circe.{Json, Encoder, Decoder}
import pprint.PPrinter

import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.wireprotocol.txns.PaymentTx

class GenericLedgerTransactionTest extends CodecTesting {

  val json =
    """
      |{
      |                    "Account" : "rJCGqCC5KMhc8Y6qso1be59sYjDMoBeJEk",
      |                    "Amount" : "2000",
      |                    "Destination" : "raxguykTevpCkjwQ1EGPwbnHtihRmgQPgH",
      |                    "Fee" : "45",
      |                    "Flags" : 2147483648,
      |                    "Sequence" : 25,
      |                    "Signers" : [
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
      |                    ],
      |                    "SigningPubKey" : "",
      |                    "TransactionType" : "Payment",
      |                    "hash" : "821367A7ADE86E07FEEDDA3B574D07CB873A022E63A4A1F2284A0A0BC362D6CB"
      |
      |                }
      |""".stripMargin

  test("Summonging") {
    import io.circe.syntax._
    val decoder: Decoder[GenericLedgerTransaction[PaymentTx]] = Decoder[GenericLedgerTransaction[PaymentTx]]
    val a: GenericLedgerTransaction[PaymentTx]                = testCompleted(parseAndDecode(json, decoder))
    val encoder: Encoder[GenericLedgerTransaction[PaymentTx]] = Encoder[GenericLedgerTransaction[PaymentTx]]
    val j: Json                                               = encoder.apply(a)
    val a2: Result[GenericLedgerTransaction[PaymentTx]]       = j.as[GenericLedgerTransaction[PaymentTx]]
    logger.info(s"A = ${PPrinter.Color(a)}")
    logger.debug(s"Manual: ${j.spaces4}")
    logger.debug(s"Auto : ${a.asJson.spaces4}")
    a2 shouldEqual Right(a)
  }

}
