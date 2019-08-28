package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.{Encoder, Decoder}
import pprint.PPrinter

import com.odenzo.ripple.models.testkit.CodecTesting

class PaymentTxTest extends CodecTesting {

  test("Decoding") {

    val json =
      """
        | {
        |                    "Account" : "rPm88mdDuXLgxzpmZPXf6wPQ1ZTHRNvYVr",
        |                    "Amount" : "20000000",
        |                    "Destination" : "rDJFnv5sEfp42LMFiX3mVQKczpFTdxYDzM",
        |                    "Fee" : "12",
        |                    "Flags" : 2147483648,
        |                    "LastLedgerSequence" : 21134467,
        |                    "Sequence" : 17844696,
        |                    "SigningPubKey" : "02A61C710649C858A03DF50C8D24563613FC4D905B141EEBE019364675929AB804",
        |                    "TransactionType" : "Payment",
        |                    "TxnSignature" : "3045022100E8B2BCB090F01DD4BBC4F340756225A415A4300B6A666E155E785E281C8BBD73022069BD498E1D3861FA7B24209DF43A1E4B8692B573D0A07C2862FB2434FAC3BA48",
        |                    "hash" : "E22F340EACC19FD35D0951C89D5403CDCA3CF333BE26DE61CA2D2B8292A8135C"
        |                    }
        |""".stripMargin

    val tx: PaymentTx = getOrFailLogging(parseAndDecode(json, Decoder[PaymentTx]))
    logger.debug(s"Decoded: ${PPrinter.Color(tx)}")
    val ej = Encoder[PaymentTx].apply(tx)
    logger.debug(s"Encoded Json: ${ej.spaces4}") // Flags are parsed incorrectly and returns as negatival cal
  }
}
