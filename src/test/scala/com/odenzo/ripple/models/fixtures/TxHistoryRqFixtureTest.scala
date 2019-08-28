package com.odenzo.ripple.models.fixtures

import java.nio.file.Path

import cats._
import cats.data._
import cats.implicits._
import io.circe.{Json, Decoder}
import io.circe.syntax._
import monocle.Optional

import com.odenzo.ripple.models.atoms.ledgertree.transactions.LedgerTransaction
import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.utils.caterrors.{AppError, AppJsonDecodingError}
import com.odenzo.ripple.models.wireprotocol.ledgerinfo.LedgerRs
import com.odenzo.ripple.models.wireprotocol.transactions.TxHistoryRs
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.RippleTransaction

/**
  * Fixtures made using the (deprecated) TxHistory command.
  */
class TxHistoryRqFixtureTest extends CodecTesting {

  test("Loading") {

    val complete = for {
      files <- findFixtureFiles("tx_history_rs", "tx_history_rs_").map(_.sortBy(_.getFileName))
      _ = logger.warn(s"Processing ${files.length} fixture files")
      decoded <- files.traverse(processOneFile)
    } yield decoded
    getOrFailLogging(complete)
  }

  def processOneFile(f: Path): Either[AppError, List[LedgerTransaction]] = {

    import io.circe.optics._
    import io.circe.optics.JsonPath._
    val _txs: Optional[Json, Vector[Json]] = root.result.txs.arr
    for {
      json   <- CirceUtils.parseAsJson(f.toFile).flatMap(json2jsonobject)
      result <- findObjectField("result", json)
      txs = _txs.getOption(json.asJson).map(_.toList).getOrElse(List.empty)
      _   = logger.debug(s"${txs.size} txs found")
      lts <- txs.zipWithIndex.traverse {
        case (txnjson, indx) =>
          logger.debug(s"txs Index $indx  ${txnjson.spaces4}")
          val decoded: Either[AppJsonDecodingError, LedgerTransaction] = decode(txnjson, Decoder[LedgerTransaction])
          logIfError(decoded)
      }
      decoded <- CirceUtils.decode(result.asJson, Decoder[TxHistoryRs])
      // _ = logger.debug(s"DEcoderd ${pprint.apply(decoded)}")
    } yield lts
  }

}
