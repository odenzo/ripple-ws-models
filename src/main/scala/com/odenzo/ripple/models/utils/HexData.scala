package com.odenzo.ripple.models.utils

import cats.Show
import io.circe.generic.extras.Configuration
import io.circe.Codec
import io.circe.generic.extras.semiauto._

/**
  *
  * @param hex Hex text, will be validated. MUst use upper-case HEX because I am opinionated and lazy
  */
case class HexData(hex: String) {
  require(HexData.isHexadecimal(hex), s"Content [$hex] was not Hexadecimal")

  /** Interprets the HexData as UTF-8 Text */
  def asText: String   = HexData.hex2string(hex)
  def sizeInBytes: Int = hex.length / 2

}

/** This is actually used in Memos instead of Java MIME stuff. Forget why, maybe ScalaJS? */
object HexData {
  private[this] val hexDigits = "ABCDEF0123456789"

  implicit val show: Show[HexData] = Show.show[HexData](hd => s"Hex: ${hd.hex}")

  def fromString(s: String): HexData    = new HexData(string2hex(s))
  def isHexadecimal(s: String): Boolean = s.forall(hexDigits.contains(_))

  protected def string2hex(str: String): String = {
    str.getBytes("UTF-8").map(_.toInt.toHexString).mkString.toUpperCase // Not so efficient
  }

  /** convert hex bytes string to normal string, assuming UTF-8 encoding */
  protected def hex2string(hex: String): String = {
    val bytes: Iterator[Byte] =
      hex.iterator
        .sliding(2, 2)
        .map((v: Seq[Char]) => Integer.parseInt(v.toString(), 16).toByte)
    new String(bytes.toArray, "UTF-8")
  }
  implicit val config: Configuration = Configuration.default
  implicit val codec: Codec[HexData] = deriveUnwrappedCodec[HexData]
}
