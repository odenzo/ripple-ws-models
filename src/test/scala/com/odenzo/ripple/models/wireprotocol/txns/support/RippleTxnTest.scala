package com.odenzo.ripple.models.wireprotocol.txns.support

import io.circe._
import io.circe.syntax._

import com.odenzo.ripple.models.atoms.{Drops, AccountSetFlag}
import com.odenzo.ripple.models.support.GenesisAccount
import com.odenzo.ripple.models.wireprotocol.txns.AccountSetTx
import cats._
import cats.data._
import cats.implicits._

import com.odenzo.ripple.models.wireprotocol.{TxOptions, RippleTxnRq}

class RippleTxnTest extends com.odenzo.ripple.models.testkit.CodecTesting {

  test("Basic") {

    val tx   = AccountSetTx(GenesisAccount.address, AccountSetFlag.asfDefaultRipple.some)
    val opts = TxOptions(fee = Drops(500))

    val txn: RippleTxnRq = RippleTxnRq(tx, opts)
    val jobj: JsonObject = txn.asJsonObject
    logger.debug(s"JOBJ: ${jobj.asJson.spaces4}")
  }
}
