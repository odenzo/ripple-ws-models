package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting

class AccountRootNodeTest extends CodecTesting {

  private val decoder = Decoder[AccountRootNode]

}
