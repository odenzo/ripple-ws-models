package com.odenzo.ripple.models.fixtures

import java.nio.file.Path

import cats._
import cats.data._
import cats.implicits._
import io.circe.syntax._

import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.utils.caterrors.ModelsLibError
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.ledgerinfo.LedgerRs

class LedgerRqFixtureTest extends CodecTesting {

  test("Loading") {

    val path  = "/ledger_rs"
    val files = List("ledger_rs_21134415.json", "ledger_rs_21134416.json")
    val completed = for {
      decoded <- files.traverse(n => processOneFile(s"$path/$n"))
    } yield decoded
    testCompleted(completed)
  }

  def processOneFile(resourcePath: String): Either[ModelsLibError, LedgerRs] = {
    for {
      data       <- loadJsonResource(resourcePath)
      jobj       <- json2jsonobject(data)
      ledgerJson <- findField("result", jobj)
      decoded    <- decode[LedgerRs](ledgerJson.asJson)
      // _ = logger.debug(s"DEcoderd ${pprint.apply(decoded)}")
    } yield decoded
  }

}
