package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe.Decoder

import com.odenzo.ripple.models.atoms.AccountKeys
import com.odenzo.ripple.models.testkit.CodecTesting

/** Note the wallet propose request is an admin command so can only be run on local server and maybe testnet(?) **/
class WalletProposeRqTest extends CodecTesting {

  private val sampleResponse =
    """{
      |  "account_id" : "ra8iBMyU6JrEVLxBBG98sWnmai3fKdTZvd",
      |  "key_type" : "secp256k1",
      |  "master_key" : "FOLD SAT ORGY PRO LAID FACT TWO UNIT MARY SHOD BID BIND",
      |  "master_seed" : "sn9tYCjBpqXgHKwJeMT1LC4fdC17d",
      |  "master_seed_hex" : "B07650EDDF46DE42F9968A1A2557E783",
      |  "public_key" : "aBPUAJbNXvxP7uiTxmCcCpVgrGjsbJ8f7hQaYPRrdajXNWXuCNLX",
      |  "public_key_hex" : "02A479B04EDF3DC29EECC89D5F795E32F851C23B402D637F5552E411C354747EB5"
      |}""".stripMargin

  test("Decoding AccountKeys") {
    testDecoding(sampleResponse, Decoder[AccountKeys])
  }

  test("Decoding Default") {
    val wrs: WalletProposeRs = testDecoding(sampleResponse, Decoder[WalletProposeRs])
    val akrs: AccountKeys    = testDecoding(sampleResponse, Decoder[AccountKeys])
    wrs.keys shouldEqual akrs
  }
}
