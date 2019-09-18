package com.odenzo.ripple.models.atoms

import java.nio.charset.StandardCharsets

import com.odenzo.ripple.models.utils.ByteUtils
import com.odenzo.ripple.models.utils.caterrors.{AppException, ModelsLibError}
import cats._
import cats.data._
import cats.implicits._
import io.circe._
import io.circe.generic.extras.semiauto._

case class Blob(hex: String)

object Blob {
  def fromASCII(str: String): Either[ModelsLibError, Blob] = {
    AppException.wrapPure("ASCII to Hex Bytes") {
      val bs: Array[Byte] = str.getBytes(StandardCharsets.US_ASCII)
      val s: String       = ByteUtils.bytes2hex(bs)
      Blob(s)
    }
  }

  implicit val codec: Codec[Blob] = deriveUnwrappedCodec[Blob]

}
case class RFC1751(v: String) extends AnyVal

object RFC1751 {
  implicit val codec: Codec[RFC1751] = deriveUnwrappedCodec[RFC1751]
}

case class Base58Checksum(v: String) extends AnyVal

object Base58Checksum {
  implicit val codec: Codec[Base58Checksum] = deriveUnwrappedCodec[Base58Checksum]
}

sealed trait UnsignedInt
case class UInt8(v: Int)     extends UnsignedInt
case class UInt16(v: Int)    extends UnsignedInt
case class UInt32(v: Long)   extends UnsignedInt
case class UInt64(v: BigInt) extends UnsignedInt

object UInt8 {
  val ZERO: UInt8                  = UInt8(0)
  implicit val codec: Codec[UInt8] = deriveUnwrappedCodec[UInt8]
}

object UInt16 {
  val ZERO: UInt16                  = UInt16(0)
  implicit val codec: Codec[UInt16] = deriveUnwrappedCodec[UInt16]
}
object UInt32 {
  val ZERO: UInt32                  = UInt32(0)
  implicit val codec: Codec[UInt32] = deriveUnwrappedCodec[UInt32]
}

object UInt64 {
  val ZERO: UInt64                  = UInt64(BigInt("0"))
  implicit val codec: Codec[UInt64] = deriveUnwrappedCodec[UInt64]
}
