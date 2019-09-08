package com.odenzo.ripple.models.fixtures

import java.nio.file.Path

import cats._
import cats.data._
import cats.implicits._
import io.circe.Json
import io.circe.syntax._
import monocle.Optional

import com.odenzo.ripple.models.atoms.ledgertree.transactions.LedgerTxn
import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.utils.caterrors.{AppError, AppJsonDecodingError}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods.TxHistoryRs

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
    testCompleted(complete)
  }

  def processOneFile(f: Path): Either[AppError, List[LedgerTxn]] = {

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
          val decoded: Either[AppJsonDecodingError, LedgerTxn] = decode[LedgerTxn](txnjson)
          logIfError(decoded)
      }
      decoded <- decode[TxHistoryRs](result.asJson)
      // _ = logger.debug(s"DEcoderd ${pprint.apply(decoded)}")
    } yield lts
  }

}
