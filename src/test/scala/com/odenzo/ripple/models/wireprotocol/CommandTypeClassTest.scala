package com.odenzo.ripple.models.wireprotocol

import com.odenzo.ripple.models.support.GenesisAccount
import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo.AccountChannelsRq

class CommandTypeClassTest extends CodecTesting {

  test("Me") {

    import CommandTypeClass._
    import CommandTypeClass.LikeRippleCommandInstances._

    case class Command(name: String)
    logger.debug(s"COmmand: $accountSet.command")

    val cmd: AccountChannelsRq = AccountChannelsRq(GenesisAccount.address, None)

    val viaObj: String = LikeRippleCommand.commandName(cmd)
    //val rscodec: Codec.AsObject[LikeRippleCommand[AccountChannelsRq]#RS] =
    //  LikeRippleCommand.decodeResponseFor(Option(cmd))

    //logger.debug(s"RSCodec ${rscodec}")
    logger.debug(s"Who Am I $cmd  ${viaObj}")
    //val whoami: String = cmd.whoami

  }
}
