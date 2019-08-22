package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe.Decoder

import com.odenzo.ripple.models.support.RippleRq
import com.odenzo.ripple.models.testkit.CodecTesting

class FeatureRqTest extends CodecTesting {

  val c: CanDeleteRq = CanDeleteRq()
  val b: RippleRq    = CanDeleteRq()

  logger.info("C: " + c)
  logger.info("B: " + b)

  test("Decoding") {
    val json = parse(Data.std)
    testDecoding(Data.std, Decoder[FeatureRs])
  }

  private object Data {
    val std =
      """{
  "07D43DCE529B15A10827E5E04943B496762F9A88E3268269D69C44BE49E21104" : {
    "enabled" : true,
    "name" : "Escrow",
    "supported" : true,
    "vetoed" : false
  },
  "08DE7D96082187F6E6578530258C77FAABABE4C20474BDB82F04B021F1A68647" : {
    "enabled" : true,
    "name" : "PayChan",
    "supported" : true,
    "vetoed" : false
  },
  "1562511F573A19AE9BD103B5D6B9E01B3B46805AEC5D3C4805C902B514399146" : {
    "enabled" : true,
    "name" : "CryptoConditions",
    "supported" : true,
    "vetoed" : false
  },
  "42426C4D4F1009EE67080A9B7965B44656D7714D104A72F9B4369F97ABF044EE" : {
    "enabled" : true,
    "name" : "FeeEscalation",
    "supported" : true,
    "vetoed" : false
  },
  "4C97EBA926031A7CF7D7B36FDE3ED66DDA5421192D63DE53FFB46E43B9DC8373" : {
    "enabled" : true,
    "name" : "MultiSign",
    "supported" : true,
    "vetoed" : false
  },
  "532651B4FD58DF8922A49BA101AB3E996E5BFBF95A913B3E392504863E63B164" : {
    "enabled" : true,
    "name" : "TickSize",
    "supported" : true,
    "vetoed" : false
  },
  "6781F8368C4771B83E8B821D88F580202BCB4228075297B19E4FDC5233F1EFDC" : {
    "enabled" : true,
    "name" : "TrustSetAuth",
    "supported" : true,
    "vetoed" : false
  },
  "740352F2412A9909880C23A559FCECEDA3BE2126FED62FC7660D628A06927F11" : {
    "enabled" : true,
    "name" : "Flow",
    "supported" : true,
    "vetoed" : false
  },
  "86E83A7D2ECE3AD5FA87AB2195AE015C950469ABF0B72EAACED318F74886AE90" : {
    "enabled" : true,
    "name" : "CryptoConditionsSuite",
    "supported" : true,
    "vetoed" : false
  },
  "9178256A980A86CF3D70D0260A7DA6402AAFE43632FDBCB88037978404188871" : {
    "enabled" : true,
    "name" : "OwnerPaysFee",
    "supported" : true,
    "vetoed" : false
  },
  "C1B8D934087225F509BEB5A8EC24447854713EE447D277F69545ABFA0E0FD490" : {
    "enabled" : true,
    "name" : "Tickets",
    "supported" : true,
    "vetoed" : false
  },
  "C6970A8B603D8778783B61C0D445C23D1633CCFAEF0D43E7DBCD1521D34BD7C3" : {
    "enabled" : true,
    "name" : "SHAMapV2",
    "supported" : true,
    "vetoed" : false
  },
  "E2E6F2866106419B88C50045ACE96368558C345566AC8F2BDF5A5B5587F0E6FA" : {
    "enabled" : true,
    "name" : "fix1368",
    "supported" : true,
    "vetoed" : false
  }
}"""
  }
}
