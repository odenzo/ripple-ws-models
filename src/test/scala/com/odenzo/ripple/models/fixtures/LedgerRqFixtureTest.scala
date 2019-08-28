package com.odenzo.ripple.models.fixtures

import java.io.File
import java.nio.file.{Files, Path}
import java.util
import java.util.stream

import cats._
import cats.data._
import cats.implicits._
import io.circe.optics.JsonPath
import io.circe.{Json, Decoder, JsonObject}
import io.circe.syntax._
import monocle.Optional

import com.odenzo.ripple.models.atoms.ledgertree.transactions.LedgerTransaction
import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.utils.caterrors.{AppException, AppError, AppJsonError}
import com.odenzo.ripple.models.wireprotocol.ledgerinfo.LedgerRs

class LedgerRqFixtureTest extends CodecTesting {

  test("Loading") {

    val complete = for {
      files <- findFixtureFiles("ledger_rs", "ledger_rs_").map(_.sortBy(_.getFileName))
      _ = logger.warn(s"Processing ${files.length} fixture files")
      decoded <- files.traverse(processOneFile)
    } yield decoded
    getOrFailLogging(complete)
  }

  def processOneFile(f: Path): Either[AppError, LedgerRs] = {
    for {
      json       <- CirceUtils.parseAsJson(f.toFile).flatMap(json2jsonobject)
      ledgerJson <- findObjectField("result", json)
      decoded    <- CirceUtils.decode(ledgerJson.asJson, Decoder[LedgerRs])
      // _ = logger.debug(s"DEcoderd ${pprint.apply(decoded)}")
    } yield decoded
  }

}
