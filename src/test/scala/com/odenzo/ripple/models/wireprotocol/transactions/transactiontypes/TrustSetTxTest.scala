package com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
class TrustSetTxTest extends CodecTesting {

  val json =
    """  {
      |    "Account" : "rMJejBabHE2o69556csDe5CkjmYtQP9Jy1",
      |    "Fee" : "10",
      |    "Flags" : 2147483648,
      |    "LastLedgerSequence" : 320,
      |    "LimitAmount" : {
      |      "currency" : "NZD",
      |      "issuer" : "rJGhGVfd1VBryKE2cLTqt22H8HSHCV6N93",
      |      "value" : "10"
      |    },
      |    "Sequence" : 2,
      |    "SigningPubKey" : "EDDD12351BD4CA0900BB40755F002E8B85FC353BA1F02C84FC62D9D7D2D8BFC2B8",
      |    "TransactionType" : "TrustSet",
      |    "TxnSignature" : "7090094A046EBE920BBFEBD34F822552EE63927029BB0B2C8D99B8973C09CF518244E0752432C19CC7EA1268F4D29E5FA5F7D7DD12DEDEDF6ACBA37C30D76C00",
      |    "hash" : "52B1AB848ADC556FEC475473867A575ACF4241E172772230395283439D673F73"

      |}""".stripMargin

  test("Codec") {

    val obj = getOrLog(parseAndDecode(json, Decoder[TrustSetTx]))
    obj.asJson
    logger.debug(s"$obj")
  }
}
