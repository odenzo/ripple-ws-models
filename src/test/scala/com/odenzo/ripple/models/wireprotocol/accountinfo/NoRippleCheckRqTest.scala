package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe.Decoder

import com.odenzo.ripple.models.testkit.CodecTesting

class NoRippleCheckRqTest extends CodecTesting {

  val decoder: Decoder[NoRippleCheckRs] = Decoder[NoRippleCheckRs]

}
