package com.odenzo.ripple.models.atoms

import java.nio.charset.StandardCharsets

import io.circe.{Encoder, Decoder}

import com.odenzo.ripple.models.utils.ByteUtils
import com.odenzo.ripple.models.utils.caterrors.{AppException, AppError}

case class Blob(hex: String)

object Blob {
  def fromASCII(str: String): Either[AppError, Blob] = {
    AppException.wrapPure("ASCII to Hex Bytes") {
      val bs: Array[Byte] = str.getBytes(StandardCharsets.US_ASCII)
      val s: String       = ByteUtils.bytes2hex(bs)
      Blob(s)
    }
  }
  implicit val encoder: Encoder[Blob] = Encoder.encodeString.contramap[Blob](_.hex)
  implicit val decoder: Decoder[Blob] = Decoder.decodeString.map(Blob.apply)
}
case class RFC1751(v: String) extends AnyVal

object RFC1751 {
  val encoder: Encoder[RFC1751]          = Encoder.encodeString.contramap[RFC1751](_.v)
  implicit val decoder: Decoder[RFC1751] = Decoder.decodeString.map(RFC1751.apply)
}

case class Base58Checksum(v: String) extends AnyVal

object Base58Checksum {
  val encoder: Encoder[Base58Checksum]          = Encoder.encodeString.contramap[Base58Checksum](_.v)
  implicit val decoder: Decoder[Base58Checksum] = Decoder.decodeString.map(Base58Checksum.apply)
}

trait UnsignedInt

case class UInt8(v: Int) extends UnsignedInt

case class UInt16(v: Int) extends UnsignedInt

/** Try out the Scala non-boxed thing (Scala Value class) but it needs a plain old class */
case class UInt32(v: Long) extends UnsignedInt

/** Swithced to BigInt and encoded in JSON as String */
case class UInt64(v: BigInt) extends UnsignedInt

object UInt8 {
  val ZERO: UInt8 = UInt8(0)

  implicit val encoder: Encoder[UInt8] = Encoder.encodeInt.contramap[UInt8](_.v)
  implicit val decoder: Decoder[UInt8] = Decoder.decodeInt.map(UInt8(_))
}

object UInt16 {
  val ZERO: UInt16                      = UInt16(0)
  implicit val encoder: Encoder[UInt16] = Encoder.encodeInt.contramap[UInt16](_.v)
  implicit val decoder: Decoder[UInt16] = Decoder.decodeInt.map(UInt16(_))
}

object UInt32 {
  val ZERO: UInt32 = UInt32(0)

  implicit val encoder: Encoder[UInt32] = Encoder.encodeLong.contramap[UInt32](_.v)
  implicit val decoder: Decoder[UInt32] = Decoder.decodeLong.map(UInt32(_))
}

object UInt64 {
  val ZERO: UInt64 = UInt64(BigInt("0"))

  implicit val encoder: Encoder[UInt64] = Encoder.encodeBigInt.contramap[UInt64](_.v)
  implicit val decoder: Decoder[UInt64] = Decoder.decodeLong.map(UInt64(_))
}
