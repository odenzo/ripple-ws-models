package com.odenzo.ripple.models.atoms.ledgertree.nodes

import com.odenzo.ripple.models.testkit.CodecTesting

/**
  * High level tests on all the LedgerNode types, in-situ without the Modified/Created/Final delta if possible.
  */
class LedgerNodeTest extends CodecTesting {

  //private val decoder = Decoder[AccountRootNode]
  // Primarly to test out all the LedgerEntry type parsing.
//
//
//  test("RippleState Fiat") {
//    val target = LedgerEntrySamples.rippleState_01
//    val json   = io.circe.parser.parse(target).right.value
//    logger.debug(s"Test JSON $json")
//    val ledgerNode = json.as[LedgerNode]
//    logger.debug("Ledger Node: " + ledgerNode)
//  }
//
//  test("Inquire AccountRoot") {
//    val json = io.circe.parser.parse(accountRoot_01).right.value
//    logger.debug(s"Test JSON $json")
//    val ledgerNode = json.as[LedgerNode]
//    logger.debug("Ledger Node: " + ledgerNode)
//  }

}
