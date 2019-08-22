package com.odenzo.ripple.models.support

import io.circe.syntax._

import com.odenzo.ripple.models.atoms.AccountAddr
import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.wireprotocol.accountinfo.WalletProposeRq

class RippleCommands$Test extends CodecTesting {

  val accAddr: AccountAddr = AccountAddr("rLqxc4bxqRVVt63kHVk22Npmq9cqHVdyR")
  val swfRO                = RippleAccountRO("Steve Main", accAddr)

  test("Basic") {
    val jsonRq = Commands.walletProposeCmd.encode(WalletProposeRq(seed = None, passphrase = Some("pewefwefhrease")))
    logger.info(s"Request: ${jsonRq.asJson.spaces2}")
  }

}
