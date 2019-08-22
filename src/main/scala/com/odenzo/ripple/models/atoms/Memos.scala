package com.odenzo.ripple.models.atoms

import cats.Show
import cats.implicits._
import io.circe._
import io.circe.syntax._

import com.odenzo.ripple.models.utils.HexData
import com.odenzo.ripple.models.utils.caterrors.OError

/**
  * Another bit of a dogs breakfast. This whole memo heirarchy
  *
  * Memos contains zero or more memos. The total size in bytes of memos in Ripple in constrained. (1K, 700K?)
  *
  * @param memos
  */
case class Memos(memos: List[Memo]) {

  def add(m: Memo): Memos = this.copy(memos = this.memos :+ m)

}

object Memos {
  def fromText(s: String): Option[Memos] = Some(Memos(List(Memo.fromText(s))))

  def empty: Memos = Memos(List.empty[Memo])

  // TODO: If memos list is empty then encode has Json.Null which will be filtered on write
  // implicit val encoder: Encoder[Memos] = Encoder.encodeList[Memo].contramap[Memos](_.memos)
  implicit val decoder: Decoder[Memos] = Decoder.decodeList[Memo].map(x => Memos(x))

  implicit val encoder: Encoder[Memos] = Encoder.instance[Memos] { memos =>
    if (memos.memos.isEmpty) Json.Null
    else {
      val encoder: Encoder[List[Memo]] = Encoder[List[Memo]]
      val fieldJson                    = encoder.apply(memos.memos)
      fieldJson
    }

  }

  implicit val show: Show[Memos] = Show.show[Memos](_.memos.map(_.show).mkString("Memos\n\t", "\n\t", "\n========"))
}

trait MemoUtils {

  /**
    * Valid characters for the MemoType and MemoFormat fields
    *
    */
  def validMemoTypeContent(s: String): Boolean = {
    s.forall(c => memoDomain.contains(c))
  }

  // Applies to MemoType and MemoFormat only. No Spaces?
  private val memoDomain =
    """ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~:/?#[]@!$&'()*+,;=%"""

}

object MemoUtils extends MemoUtils

/**
  * One of the three fields is technically needed. This is the Ripple formatted memo.
  *
  * MemoUtils has some unpacking, but leave it to business layer to handle application specific memos
  * in general.
  *
  * @param data
  * @param format
  * @param mtype
  */
case class Memo(data: MemoData, format: Option[MemoFormat] = None, mtype: Option[MemoType] = None) {

  def withType(t: MemoType): Memo     = copy(mtype = Some(t))
  def withFormat(f: MemoFormat): Memo = copy(format = Some(f))
  def withContent(d: MemoData): Memo  = copy(data = d)

}

object Memo extends MemoUtils {

  /** Creates a RippleMemo with No MemoType and MemoFormat testUTF8 */
  def fromText(s: String): Memo  = Memo(MemoData.fromText(s), Some(MemoFormat.textUTF8))
  def fromJson(json: Json): Memo = Memo(MemoData.fromJson(json), Some(MemoFormat.json))

  implicit val show: Show[Memo] = Show.show[Memo] { m =>
    // Depending on the type we want to interpret and show data
    val formattedData = m.format match {
      case Some(MemoFormat.textUTF8) => m.data.hex.asText
      case Some(MemoFormat.json)     => io.circe.parser.parse(m.data.hex.asText).fold(e => "Invalid JSON", j => j.spaces2)
      case other                     => m.data.hex.show
    }

    s"Memo ${m.format.show}  ${m.mtype.show}:: \n$formattedData"

  }

  /**
    * Memos is an array of JsonObjects, each object has a single Memo subobject. Odd.
    * Think I will add in here.
    */
  implicit val encoder: Encoder[Memo] = new Encoder[Memo] {
    final def apply(v: Memo): Json = {
      val fields = Seq(
        Some(("MemoData", v.data.hex.asJson)),
        v.format.map(f => ("MemoFormat", f.format.asJson)),
        v.mtype.map(t => ("MemoType", t.mtype.asJson))
      ).flatten

      val theMemo = JsonObject.fromIterable(fields).asJson
      JsonObject.singleton("Memo", theMemo).asJson
    }
  }

  implicit val decoder: Decoder[Memo] = new Decoder[Memo] {

    final def apply(c: HCursor): Decoder.Result[Memo] = {
      val cursor = c.downField("Memo")
      cursor.get[HexData]("MemoData").map(hex => MemoData(hex)).map { data =>
        Memo(
          data,
          cursor.get[HexData]("MemoFormat").toOption.map(hex => MemoFormat(hex)),
          cursor.get[HexData]("MemoType").toOption.map(hex => MemoType(hex))
        )
      }
    }
  }
}

/**
  * Here we may want overloaded constructors, and also some Factory methods.
  * Not going to worry about making it very flexbile and this point.
  *
  * @param hex Arbitrary length Hex (String)
  */
case class MemoData(hex: HexData) {
  def sizeInBytes: Int = hex.sizeInBytes
}

object MemoData {
  def fromJson(j: Json): MemoData   = fromText(j.noSpaces)
  def fromText(s: String): MemoData = MemoData(HexData.fromString(s))
}

case class MemoFormat(format: HexData) {
  def sizeInBytes: Int = format.sizeInBytes
}

/**
  * The format is by conventions saying what/how the actual memo contents are encoded.
  * Per docs we use MIME types for now, but can use anything reall, including case object codec hints etc.
  */
object MemoFormat extends MemoUtils {

  implicit val show: Show[MemoFormat] = Show.show[MemoFormat](mf => s"MemoFormat ${mf.format.asText}")

  val textUTF8: MemoFormat = unsafeFromText("""text/plain;charset=UTF-8""")
  val json: MemoFormat     = unsafeFromText("application/json")

  /** Creates a MemoFormat making sure that all char are english alpha-numeric plus a limited set of symbols */
  def fromText(s: String): Either[OError, MemoFormat] = {
    Either.cond(
      MemoUtils.validMemoTypeContent(s),
      unsafeFromText(s),
      OError("Invalid alpha-numerics before converting to Hex")
    )
  }

  /** Ripple constrains the allowed characters. This will succeed always, but if bad then fail with Ripple Error later */
  private def unsafeFromText(s: String): MemoFormat = {
    MemoFormat(HexData.fromString(s))
  }

}

/**
  * Hex value representing characters allowed in URLs. Conventionally,
  * a unique relation (according to RFC 5988) that defines the format of this memo.
  * Please see MemoType object
  */
case class MemoType(mtype: HexData) {
  def sizeInBytes: Int = mtype.sizeInBytes

}

/**
  * Some memo types for starters
  */
object MemoType {

  implicit val show: Show[MemoType] = Show.show[MemoType](mt => s"MemoType ${mt.mtype.asText}")

  val DefaultMemo: MemoType  = unsafeFromText("Default")
  val SystemMemo: MemoType   = unsafeFromText("com.odenzo/system")
  val MerchantMemo: MemoType = unsafeFromText("com.odenzo/merchant")
  val CustomerMemo: MemoType = unsafeFromText("com.odenzo/customer")

  /** Creates a MemoFormat making sure that all char are english alpha-numeric plus a limited set of symbols */
  def fromText(s: String): Either[OError, MemoType] = {
    Either.cond(
      MemoUtils.validMemoTypeContent(s),
      unsafeFromText(s),
      OError("Invalid alpha-numerics before converting to Hex")
    )
  }

  /** Ripple constrains the allowed characters. This will succeed always, but if bad then fail with Ripple Error later */
  private def unsafeFromText(s: String): MemoType = {
    MemoType(HexData.fromString(s))
  }
}
