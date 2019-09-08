package com.odenzo.ripple.models.fixtures

import java.nio.file.Path

import cats._
import cats.data._
import cats.implicits._
import io.circe.syntax._

import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.utils.caterrors.AppError
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.ledgerinfo.LedgerRs

class LedgerRqFixtureTest extends CodecTesting {

  test("Loading") {

    val complete = for {
      files <- findFixtureFiles("ledger_rs", "ledger_rs_").map(_.sortBy(_.getFileName))
      _ = logger.warn(s"Processing ${files.length} fixture files")
      decoded <- files.traverse(processOneFile)
    } yield decoded
    testCompleted(complete)
  }

  def processOneFile(f: Path): Either[AppError, LedgerRs] = {
    for {
      json       <- CirceUtils.parseAsJson(f.toFile).flatMap(json2jsonobject)
      ledgerJson <- findObjectField("result", json)
      decoded    <- decode[LedgerRs](ledgerJson.asJson)
      // _ = logger.debug(s"DEcoderd ${pprint.apply(decoded)}")
    } yield decoded
  }

}
