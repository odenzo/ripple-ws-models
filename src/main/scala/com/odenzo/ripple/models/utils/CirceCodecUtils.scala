package com.odenzo.ripple.models.utils

import cats.implicits._
import io.circe.generic.extras.Configuration

import com.odenzo.ripple.models.atoms.{LedgerID, SignerEntry}
import monocle.Lens
import scribe.Logging

import com.odenzo.ripple.models.wireprotocol.commands.RippleRq
import com.odenzo.ripple.models.wireprotocol.txns.{RippleTx, RippleTxnType}

/** Helps for writing Encoders and Decoders. See CirceUtils for non-codec helpers.
  * I am very slowly working towards making type-classes, AccountData is my play-toy
  *  type ME = AccountData  vs AccountData.type
  *  Thinking making a   CommandDeriver.... but still not sure. Need to get things working again first.
 **/
trait CirceCodecUtils extends Logging {

  import io.circe._
  import io.circe.syntax._

  def wrapCommandCodec[T <: RippleRq](command: String, codec: Codec.AsObject[T]): Codec.AsObject[T] = {
    val encoder: Encoder.AsObject[T] = wrapCommandEncoder(codec, command)
    val decoder: Decoder[T]          = codec
    Codec.AsObject.from(decoder, encoder)
  }

  /** This adds "command" = .. field  */
  def wrapCommandEncoder[A <: RippleRq](enc: Encoder.AsObject[A], command: String): Encoder.AsObject[A] = {

    val mapper                  = withCommand(command)
    val e2: Encoder.AsObject[A] = enc.mapJsonObject(mapper)
    e2
  }

  def wrapTxnCodec[A <: RippleTx](codec: Codec.AsObject[A], txnName: RippleTxnType): Codec.AsObject[A] = {
    val enc: Encoder.AsObject[A] = codec.mapJsonObject(withTxnType(txnName))
    val c2: Codec.AsObject[A]    = Codec.AsObject.from(codec, enc)
    c2
  }

  def wrapCommandEncoderWithLedgerID[A <: RippleRq](enc: Encoder.AsObject[A], command: String): Encoder.AsObject[A] = {

    val mapper                  = withCommandAndLedgerID(command)
    val e2: Encoder.AsObject[A] = enc.mapJsonObject(mapper)
    e2
  }

  def wrapCommandDecoder[A <: RippleRq](codec: Codec.AsObject[A])(implicit lens: Lens[A, LedgerID]): Decoder[A] = {
    val c2: Decoder[(A, LedgerID)] = codec.product(LedgerID.objDecoder)
    val updated: Decoder[A]        = c2.map { case (a, l: LedgerID) => lens.set(l)(a) }
    updated
  }

//  def wrapCOmmandEncoderPartial[A <: RippleRq](codec: Codec.AsObject[LedgerID => A]): Encoder[A] = {
//    val codecEncoder: Encoder[LedgerID => A] = codec.mapJson(identity)
//    val ledgerId: LedgerID                   = LedgerSequence(0)
//
//  }
  def wrapCommandDecoderPartial[A <: RippleRq](decoder: Decoder[LedgerID => A]): Decoder[A] = {

    val c2: Decoder[LedgerID] = LedgerID.objDecoder
    val c3: Decoder[A]        = decoder.product(c2).map { case (f, l) => f(l) }
    c3
  }

  def wrapCommandCodecWithLens[A <: RippleRq](codec: Codec.AsObject[A], command: String)(
      implicit lens: Lens[A, LedgerID]
  ): Codec[A] = {

    val enc: Encoder.AsObject[A] = wrapCommandEncoderWithLedgerID(codec, command)
    val dec: Decoder[A]          = wrapCommandDecoder(codec)
    Codec.from(dec, enc)
  }

  def wrapListOfNestedObj[A: Codec.AsObject](name: String): Codec[List[A]] = {
    val unwrapper: Decoder[List[A]] = Decoder.decodeList[A](Decoder[A].prepare(_.downField(name)))
    val wrapper: Encoder[List[A]] = Encoder.encodeList[A](
      Encoder.AsObject[A].mapJsonObject(b => JsonObject.singleton(name, b.asJson))
    )
    Codec.from(unwrapper, wrapper)
  }

  // ------------- Functions applied to field names -----------------

  /** Transformation to capitalize the first letter only of a field name */
  val capitalizeTransform: String => String = (s: String) => s.capitalize
  val uncapitalizeTransform: String => String = (s: String) => {
    if (s == null || s.length == 0 || s.charAt(0).isLower) s else s.updated(0, s.charAt(0).toLower)
  }

  def changeFieldName(name: String, newName: String)(in: JsonObject): JsonObject = {
    in(name).map(oldVal => in.add(newName, oldVal)).map(jo => jo.remove(name)).getOrElse(in)
  }

  // --------- Customizable and Pre-Built Configurations --------------
  val configCapitalize: Configuration = Configuration.default.copy(transformMemberNames = capitalizeTransform)

  val configWithTipe = Configuration.default.copy(transformMemberNames = (s: String) => if (s == "tipe") "type" else s)
  def configCapitalizeExcept(skip: Set[String] = Set("hash", "index")): Configuration = {
    def fn(s: String): String = {
      s match {
        case skipped if skip.contains(skipped) => skipped
        case other                             => capitalizeTransform(other)
      }
    }
    Configuration.default.copy(transformMemberNames = fn)
  }

  //--------- JsonObject mappers (typically used with Encoders) ---------------
  def withCommand(cmd: String): JsonObject => JsonObject            = withField("command", cmd)
  def withTxnType(txn: RippleTxnType): JsonObject => JsonObject     = withField("TransactionType", txn.toString)
  def withLiftLedgerID: JsonObject => JsonObject                    = liftAllFields("ledger", _)
  def withCommandAndLedgerID(cmd: String): JsonObject => JsonObject = withCommand(cmd) andThen withLiftLedgerID
  def withMappedTipe: JsonObject => JsonObject                      = withRenameField("tipe", "type")

  /** Mapping to add the specified field and value to incoming JsonObject */
  def withField[A: Encoder](name: String, v: A)(in: JsonObject): JsonObject = (name := v) +: in

  def withRenameField(old: String, to: String): JsonObject => JsonObject = changeFieldName(old, to)

  /**
    *  Usefule with case class Foo(a:A, b:B) which should both be decoded from the same (top) hcurser
    */
  def combineEncodedJsonObjects[A, B](
      a: A,
      b: B
  )(implicit e1: Encoder.AsObject[A], e2: Encoder.AsObject[B]): JsonObject = {
    JsonObject.fromIterable(a.asJsonObject.toVector ++ a.asJsonObject.toVector)
  }

  /**
    * Looks for field `toAddField` in `in` with JsonObject value. Moves all subfield to in and deletes
    * `toAddField` from `in`
    * This can result in duplicate field names in case same field in sub-object as in.
    * @param toAddField Field name of in which must contain a JsonObject. If not, no-op
    * @param in The top level JsonObject, optionally containing `toAddFields`
    * @return   All the fields in toAdd field moved into in, using the original field names.
    **/
  def liftAllFields(toAddField: String, in: JsonObject): JsonObject = {
    in(toAddField).flatMap(_.asObject) match {
      case None      => in
      case Some(obj) => JsonObject.fromIterable(obj.toVector ++ in.remove(toAddField).toVector)
    }
  }

}

object CirceCodecUtils extends CirceCodecUtils
