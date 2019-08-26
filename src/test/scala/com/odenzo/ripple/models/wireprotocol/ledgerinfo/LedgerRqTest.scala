package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe.Encoder

import com.odenzo.ripple.models.atoms.{LedgerHash, LedgerSequence, RippleHash}
import com.odenzo.ripple.models.testkit.CodecTesting

class LedgerRqTest extends CodecTesting {

  test("Encoding Default Name") {
    testEncoding(LedgerRq(transactions = true), Encoder[LedgerRq])
    // Assert json has ledger_index and not ledger_hash
  }

  test("Encoding with index") {
    val rq = LedgerRq(transactions = true, ledger = LedgerSequence(666))
    testEncoding(rq, Encoder[LedgerRq])
  }

  test("Encoding with hash") {
    val rq = LedgerRq(transactions = true, ledger = LedgerHash(RippleHash("fweoijfweoifjwe")))
    testEncoding(rq, Encoder[LedgerRq])
  }
}
