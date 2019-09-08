package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods

import io.circe.Encoder

import com.odenzo.ripple.models.atoms.{Currency, Script}
import com.odenzo.ripple.models.support.GenesisAccount
import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.pathandorderbook.BookOffersRq

class BookOffersRqTest extends CodecTesting {

  test("Customer Encoder") {
    val script = Script(Currency.NZD, GenesisAccount.address)
    testEncoding(BookOffersRq(taker_gets = script, taker_pays = script), Encoder[BookOffersRq])
  }

  val sample = """ {
  "id" : "7f424596-bdb6-4e91-bdca-5b9353146d8a",
  "result" : {
    "ledger_hash" : "F4CD98650A0862F9D355467EFA5A4CC7E1FB07D47D8F8179C4618532C6DE56D0",
    "ledger_index" : 61888,
    "offers" : [
      {
        "Account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
        "BookDirectory" : "C8131AB5238A6F807A6764777233255505AE7101412BAC1459055441753DDFF2",
        "BookNode" : "0000000000000000",
        "Expiration" : 551551965,
        "Flags" : 0,
        "LedgerEntryType" : "Offer",
        "OwnerNode" : "0000000000000000",
        "PreviousTxnID" : "86B7D03003E9AD10B5EA660F8187F30CE1D85D0B84B2F9C831DE584DD82315EE",
        "PreviousTxnLgrSeq" : 39417,
        "Sequence" : 32,
        "TakerGets" : {
          "currency" : "NZD",
          "issuer" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
          "value" : "666.66"
        },
        "TakerPays" : "10000000",
        "index" : "01327DF6343A6950443485F83E24358CCD1B94BCC82EE1F9DCAC682443706ABD",
        "owner_funds" : "666.66",
        "quality" : "15000.15000150002"
      },
      {
        "Account" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
        "BookDirectory" : "C8131AB5238A6F807A6764777233255505AE7101412BAC1459055441753DDFF2",
        "BookNode" : "0000000000000000",
        "Expiration" : 551553187,
        "Flags" : 0,
        "LedgerEntryType" : "Offer",
        "OwnerNode" : "0000000000000000",
        "PreviousTxnID" : "71044927B0C2814F3C80CD8EF8E4D69ABEF349798112172A25296E5DE08F96ED",
        "PreviousTxnLgrSeq" : 39823,
        "Sequence" : 38,
        "TakerGets" : {
          "currency" : "NZD",
          "issuer" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
          "value" : "666.66"
        },
        "TakerPays" : "10000000",
        "index" : "474E8CA3AB366DF511B630B3C5D1F3A0D3ECCCBC03EC1B8229C850C8CAC8ABAC",
        "owner_funds" : "666.66",
        "quality" : "15000.15000150002"
      }
    ],
    "validated" : true
  },
  "status" : "success",
  "type" : "response"
}"""
}
