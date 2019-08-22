package com.odenzo.ripple.models.atoms

import io.circe.Json
import io.circe.syntax._

import com.odenzo.ripple.models.testkit.CodecTesting

class RippleLogLevelTest extends CodecTesting {

  test("Basic") {
    import RippleLogLevel._
    val x: _root_.com.odenzo.ripple.models.atoms.RippleLogLevel = RippleLogLevel.ERROR
    x.asJson shouldEqual Json.fromString("error")
  }

  val sample = """  {
  "id" : "02fa195b-6236-4710-b56d-ca81e8ffb7e6",
  "result" : {
    "levels" : {
      "Amendments" : "Info",
      "Application" : "Info",
      "Collector" : "Info",
      "Consensus" : "Info",
      "FeeVote" : "Info",
      "Flow" : "Info",
      "InboundLedger" : "Info",
      "JobQueue" : "Info",
      "Ledger" : "Info",
      "LedgerCleaner" : "Info",
      "LedgerConsensus" : "Info",
      "LedgerHistory" : "Info",
      "LedgerMaster" : "Info",
      "LedgerTiming" : "Info",
      "LoadManager" : "Info",
      "LoadMonitor" : "Info",
      "ManifestCache" : "Info",
      "NetworkOPs" : "Info",
      "NodeObject" : "Info",
      "OpenLedger" : "Info",
      "OrderBookDB" : "Info",
      "Overlay" : "Info",
      "PathRequest" : "Info",
      "Peer" : "Info",
      "PeerFinder" : "Info",
      "Protocol" : "Info",
      "RPCHandler" : "Info",
      "Resolver" : "Info",
      "Resource" : "Info",
      "SHAMap" : "Info",
      "SHAMapStore" : "Info",
      "Server" : "Info",
      "TaggedCache" : "Info",
      "TimeKeeper" : "Info",
      "TransactionAcquire" : "Info",
      "TxMeta" : "Info",
      "TxQ" : "Info",
      "Validations" : "Info",
      "ValidatorList" : "Info",
      "ValidatorSite" : "Info",
      "View" : "Info",
      "WALCheckpointer" : "Info",
      "base" : "Info"
    }
  },
  "status" : "success",
  "type" : "response"
}"""
}
