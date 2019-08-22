package com.odenzo.ripple.models.atoms

import enumeratum.values.CirceValueEnum
import io.circe.JsonNumber
import spire.math.UInt
import io.circe.syntax._

import com.odenzo.ripple.models.testkit.CodecTesting

class BitMaskTest extends CodecTesting {

  test("Max Flag?") {
    logger.debug(s"Max Int: ${Int.MaxValue}  Max UINT: ${UInt.MaxValue}")
    val num: JsonNumber = JsonNumber.fromIntegralStringUnsafe("2147483648")
    val json            = num.asJson
    import BitMask._
    val uint = BitMask.uIntDecoder.decodeJson(json)

    logger.debug(s"UINT: $uint")
    val res: BitMask[BitFlag] = getOrLog(json.as[BitMask[BitFlag]])
  }

  test("Bits2Flags") {

    val mask = BitMask[TrustSetFlag](UInt.MaxValue)

    val obj: IndexedSeq[TrustSetFlag] = TrustSetFlag.asFlags(mask.xor(TrustSetFlag.tfSetNoRipple))
    val aFlag: TrustSetFlag           = obj.head

    logger.info(s"Object $obj")

  }
}
