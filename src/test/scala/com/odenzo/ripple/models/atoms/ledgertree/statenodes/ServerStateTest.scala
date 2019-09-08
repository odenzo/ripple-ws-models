package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.Decoder

import com.odenzo.ripple.models.atoms.ServerState
import com.odenzo.ripple.models.testkit.CodecTesting

class ServerStateTest extends CodecTesting {

  test("Decoding") {
    val json =
      """
        |{
        |      "build_version": "0.30.1-rc3",
        |      "complete_ledgers": "18611104-18615049",
        |      "io_latency_ms": 1,
        |      "last_close": {
        |        "converge_time": 3003,
        |        "proposers": 5
        |      },
        |      "load": {
        |        "job_types": [
        |          {
        |            "job_type": "untrustedProposal",
        |            "peak_time": 1,
        |            "per_second": 3
        |          },
        |          {
        |            "in_progress": 1,
        |            "job_type": "clientCommand"
        |          },
        |          {
        |            "avg_time": 12,
        |            "job_type": "writeObjects",
        |            "peak_time": 345,
        |            "per_second": 2
        |          },
        |          {
        |            "job_type": "trustedProposal",
        |            "per_second": 1
        |          },
        |          {
        |            "job_type": "peerCommand",
        |            "per_second": 64
        |          },
        |          {
        |            "avg_time": 33,
        |            "job_type": "diskAccess",
        |            "peak_time": 526
        |          },
        |          {
        |            "job_type": "WriteNode",
        |            "per_second": 55
        |          }
        |        ],
        |        "threads": 6
        |      },
        |      "load_base": 256,
        |      "load_factor": 256000,
        |      "peers": 10,
        |      "pubkey_node": "n94UE1ukbq6pfZY9j54sv2A1UrEeHZXLbns3xK5CzU9NbNREytaa",
        |      "pubkey_validator": "n9KM73uq5BM3Fc6cxG3k5TruvbLc8Ffq17JZBmWC4uP4csL4rFST",
        |      "server_state": "proposing",
        |      "server_state_duration_us": 92762334,
        |      "state_accounting": {
        |        "connected": {
        |          "duration_us": "150510079",
        |          "transitions": 1
        |        },
        |        "disconnected": {
        |          "duration_us": "1827731",
        |          "transitions": 1
        |        },
        |        "full": {
        |          "duration_us": "168295542987",
        |          "transitions": 1865
        |        },
        |        "syncing": {
        |          "duration_us": "6294237352",
        |          "transitions": 1866
        |        },
        |        "tracking": {
        |          "duration_us": "13035524",
        |          "transitions": 1866
        |        }
        |      },
        |      "uptime": 174748,
        |      "validated_ledger": {
        |        "base_fee": 10,
        |        "close_time": 507693650,
        |        "hash": "FEB17B15FB64E3AF8D371E6AAFCFD8B92775BB80AB953803BD73EA8EC75ECA34",
        |        "reserve_base": 20000000,
        |        "reserve_inc": 5000000,
        |        "seq": 18615049
        |      },
        |      "validation_quorum": 4,
        |      "validator_list_expires": 561139596
        |    }
        |""".stripMargin

    val done = parseAsJObj(json).flatMap(decodeObj(_)(Decoder[ServerState]))
    testCompleted(done)
    done.foreach(v => scribe.debug(s"SS:  ${pprint.apply(v)}"))
  }
}
