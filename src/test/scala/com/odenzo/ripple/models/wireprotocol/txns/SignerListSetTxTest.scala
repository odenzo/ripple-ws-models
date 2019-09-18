package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting
import io.circe.literal._

import com.odenzo.ripple.models.utils.caterrors.AppJsonDecodingError
class SignerListSetTxTest extends CodecTesting {

  val json =
    json"""
               {
  "Account" : "r4NytfX4dbkTNYghq4WgT2QMhMucqwHwBz",
  "Fee" : "10",
  "Flags" : 2147483648,
  "Sequence" : 1,
  "SignerEntries" : [
    {
      "SignerEntry" : {
        "Account" : "rBsTfBtbz1N4uX6pA8feG1k2vzMiarJ8E4",
        "SignerWeight" : 1
      }
    },
    {
      "SignerEntry" : {
        "Account" : "rPeiNmqmLLH8FdLL1q2q76s5TMLNA9PCd4",
        "SignerWeight" : 1
      }
    },
    {
      "SignerEntry" : {
        "Account" : "rfraPRuqdZhyiEN47qMLQFc9yMtfiKUnxA",
        "SignerWeight" : 1
      }
    }
  ],
  "SignerQuorum" : 2,
  "SigningPubKey" : "026FD9DD50F9A2447DF4F5A49F12F3C38165254A3CCDA79EF1F86B207827E1D446",
  "TransactionType" : "SignerListSet",
  "TxnSignature" : "3045022100A1D6E6D323C69405CA8F35083C5A7A58A289E4F20D3668BED697691CF1FD8217022061CCF977F45BCB0364BD1ACE7F05D11124AFDB74B4D7E8B8E1183431853D16DF",
  "hash" : "5F813B99FCB56199C900A3F30D6F303B5D9C239EF450DA5B65D94789B396911F",
       "TransactionIndex" : 3,
    "TransactionResult" : "tesSUCCESS"

        }
    """

  test("Decoding from Ledger") { // This is the reponse from LedgerRq(transactions=true)

    val obj: Either[AppJsonDecodingError, SignerListSetTx] = decode(json)(Decoder[SignerListSetTx])
    val o                                                  = testCompleted(obj)
    logger.info(s"${pprint.apply(o)}")
  }
}
