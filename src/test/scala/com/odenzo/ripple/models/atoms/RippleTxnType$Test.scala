package com.odenzo.ripple.models.atoms

import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.wireprotocol.txns.RippleTxnType

class RippleTxnType$Test extends CodecTesting {

  test("Product Stuff") {
    RippleTxnType.values.foreach((i: RippleTxnType) => logger.info(s"Value: $i => ${i.enumEntry}"))
    logger.info("Product Prefix: " + RippleTxnType.PaymentChannelClaim.productPrefix)
  }
  test("ToString") {
    logger.debug(s"${RippleTxnType.PaymentChannelClaim}")
  }
}
