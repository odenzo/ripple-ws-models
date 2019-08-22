package com.odenzo.ripple.models.atoms

import scala.collection.immutable

import cats.implicits._
import enumeratum._
import enumeratum.values.{StringEnum, StringEnumEntry}
import io.circe.Decoder.Result
import io.circe.generic.semiauto.deriveDecoder
import io.circe.{Decoder, Encoder, Json, JsonObject}
import scribe.Logging

import com.odenzo.ripple.models.utils.CirceCodecUtils

sealed trait RippleLogLevel extends EnumEntry

case object RippleLogLevel extends Enum[RippleLogLevel] with CirceEnum[RippleLogLevel] {

  case object TRACE extends RippleLogLevel() //"trace")

  case object DEBUG extends RippleLogLevel //"debug")

  case object INFO extends RippleLogLevel //"info")

  case object WARNING extends RippleLogLevel //"warn")   "Warning" now.

  case object ERROR extends RippleLogLevel //"error")

  case object FATAL extends RippleLogLevel //"fatal")

  val values: immutable.IndexedSeq[RippleLogLevel] = findValues

  implicit val encoder: Encoder[RippleLogLevel] = Circe.encoderLowercase(enum = this)

  implicit val decoder: Decoder[RippleLogLevel] = Circe.decodeCaseInsensitive(this)
}

// TODO: Lowest Priority I actually am tired of the all caps for constants. FistCap enough I think.
sealed abstract class RippleLogPartition(val value: String) extends StringEnumEntry

case object RippleLogPartition extends StringEnum[RippleLogPartition] {

  val values: immutable.IndexedSeq[RippleLogPartition] = findValues

  case object AMENDMENTS          extends RippleLogPartition("Amendments")
  case object APPLICATION         extends RippleLogPartition("Application")
  case object COLLECTOR           extends RippleLogPartition("Collector")
  case object CONNECTION_IMPL     extends RippleLogPartition("ConnectionImpl")
  case object CONSENSUS           extends RippleLogPartition("Consensus")
  case object FEE_VOTE            extends RippleLogPartition("FeeVote")
  case object FLOW                extends RippleLogPartition("Flow")
  case object HANDLER_LOG         extends RippleLogPartition("HandlerLog")
  case object INBOUND_LEDGER      extends RippleLogPartition("InboundLedger")
  case object JOB_QUEUE           extends RippleLogPartition("JobQueue")
  case object LEDGER              extends RippleLogPartition("Ledger")
  case object LEDGER_CLEANER      extends RippleLogPartition("LedgerCleaner")
  case object LEDGER_CONSENSUS    extends RippleLogPartition("LedgerConsensus")
  case object LEDGER_HISTORY      extends RippleLogPartition("LedgerHistory")
  case object LEDGER_MASTER       extends RippleLogPartition("LedgerMaster")
  case object LEDGER_TIMING       extends RippleLogPartition("LedgerTiming")
  case object LOAD_MANAGER        extends RippleLogPartition("LoadManager")
  case object LOAD_MONITOR        extends RippleLogPartition("LoadMonitor")
  case object MANIFEST_CACHE      extends RippleLogPartition("ManifestCache")
  case object NETWORK_OPS         extends RippleLogPartition("NetworkOPs")
  case object NODE_OBJECT         extends RippleLogPartition("NodeObject")
  case object OPEN_LEDGER         extends RippleLogPartition("OpenLedger")
  case object ORDER_BOOK_DB       extends RippleLogPartition("OrderBookDB")
  case object OVERLAY             extends RippleLogPartition("Overlay")
  case object PATH_REQUEST        extends RippleLogPartition("PathRequest")
  case object PEER                extends RippleLogPartition("Peer")
  case object PEER_FINDER         extends RippleLogPartition("PeerFinder")
  case object PERF_LOG            extends RippleLogPartition("PerfLog")
  case object PROTOCOL            extends RippleLogPartition("Protocol")
  case object RPC_HANDLER         extends RippleLogPartition("RPCHandler")
  case object RESOLVER            extends RippleLogPartition("Resolver")
  case object RESOURCE            extends RippleLogPartition("Resource")
  case object SHA_MAP             extends RippleLogPartition("SHAMap")
  case object SHA_MAP_STORE       extends RippleLogPartition("SHAMapStore")
  case object SERVER              extends RippleLogPartition("Server")
  case object TAGGED_CACHE        extends RippleLogPartition("TaggedCache")
  case object TIME_KEEPER         extends RippleLogPartition("TimeKeeper")
  case object TRANSACTION_ACQUIRE extends RippleLogPartition("TransactionAcquire")
  case object TX_META             extends RippleLogPartition("TxMeta")
  case object TXQ                 extends RippleLogPartition("TxQ")
  case object UNIQUE_NODE_LIST    extends RippleLogPartition("UniqueNodeList")
  case object VALIDATIONS         extends RippleLogPartition("Validations")
  case object VALIDATOR_LIST      extends RippleLogPartition("ValidatorList")
  case object VALIDATOR_SITE      extends RippleLogPartition("ValidatorSite")
  case object VIEW                extends RippleLogPartition("View")
  case object WAL_CHECKPOINTER    extends RippleLogPartition("WALCheckpointer")
  case object WEB_SOCKET          extends RippleLogPartition("WebSocket")
  case object BASE                extends RippleLogPartition("base")

  implicit val encoder: Encoder[RippleLogPartition] = Encoder.encodeString.contramap(_.value)
  implicit val decoder: Decoder[RippleLogPartition] = Decoder.decodeString
    .emap(v => Either.fromOption(this.withValueOpt(v), s"$v doesn't match a parition"))

}

case class RippleLogLevels(partions: List[RippleLogSetting])

/** Setting per partition, encoded are field:val in enclosing json object (levels) */
case class RippleLogSetting(part: RippleLogPartition, level: RippleLogLevel)

object RippleLogSetting {

  implicit val decoder: Decoder[RippleLogSetting] = deriveDecoder[RippleLogSetting]
}

object RippleLogLevels extends CirceCodecUtils with Logging {
//
//
//  /** The actual conversion which has a  "key": "value" like Json structure
//
//    **/
//  def convert(logPartition: String, levelStringAsJson: Json): Either[String, RippleLogSetting] = {
//    // Wacked approach for now is to use Json Decoders for both the Key and the value
//    val keyJson: Json = Json.fromString(logPartition)
//    val partition: Result[RippleLogPartition] = keyJson.as[RippleLogPartition] // To an enumeration now.
//    val level: Result[RippleLogLevel] = levelStringAsJson.as[RippleLogLevel]
//    val screamed: Result[RippleLogSetting] = (partition, level).mapN(RippleLogSetting.apply)
//    screamed.leftMap(err => err.show)
//  }
//
//  // Upgrade cats work. traverseU no longer applicable. traverse should work but doesn't yet
//
//  /**
//    * Applied on the levels object within result:
//    * *
//    * * <pre>
//    * * "result" : {
//    * * "levels" : {
//    * * "Amendments" : "Info",
//    * * "Application" : "Info",
//    * * "Collector" : "Info",
//    * * "Consensus" : "Info",
//    * * "FeeVote" : "Info",
//    * * "Flow" : "Info",
//    * * ...
//    * * </pre>
//    */
  implicit val decoder: Decoder[RippleLogLevels] = Decoder.decodeJsonObject.emap[RippleLogLevels] {
    levelsObj: JsonObject =>
      val eachEntry
          : List[(String, Json)] = levelsObj.toList // Key and value I think. and then pass on to RippleLogLevel?

      val settings: Result[List[RippleLogSetting]] = eachEntry.traverse {
        case (key: String, value: Json) => convert(key, value)
      }

      settings.leftMap(_.toString()).map((l: List[RippleLogSetting]) => RippleLogLevels(l))
  }

  def convert(key: String, value: Json): Result[RippleLogSetting] = {
    val logLevel: Result[RippleLogLevel]   = value.as[RippleLogLevel]
    val logKey: Result[RippleLogPartition] = Json.fromString(key).as[RippleLogPartition]
    (logKey, logLevel).mapN(RippleLogSetting.apply)
  }

}
