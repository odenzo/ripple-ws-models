package com.odenzo.ripple.models.wireprotocol.ledgerinfo

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting

/**
  * Used to test both LedgerEntry inquiry and see if they match ledgernodes as defined.
  */
class LedgerEntryRqTest extends CodecTesting {

  val decoder: Decoder[LedgerEntryRs] = Decoder[LedgerEntryRs]

}
