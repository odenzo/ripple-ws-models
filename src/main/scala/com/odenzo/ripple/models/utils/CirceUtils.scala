package com.odenzo.ripple.models.utils

import java.io.File

import cats._
import cats.implicits._
import io.circe.Decoder.Result
import io.circe._
import io.circe.jawn.JawnParser
import io.circe.syntax._
import scribe.Logging

import com.odenzo.ripple.models.utils.caterrors.CatsTransformers.ErrorOr
import com.odenzo.ripple.models.utils.caterrors.{AppError, AppException, AppJsonDecodingError, AppJsonParsingError}

/**
  *  Traits for working with Circe Json / DOM
  */
trait CirceUtils extends Logging {

  /** Ripled doesn't like objects like { x=null } */
  val droppingNullsPrinter: Printer = Printer.spaces2.copy(dropNullValues = true)

  /** Converts json to formatted text dropping null JsonObject fields.
    *  @param json
    *  @return
    */
  def print(json: Json): String = json.pretty(droppingNullsPrinter)

  def printObj(jsonObject: JsonObject): String = print(jsonObject.asJson)

  /** Caution: Uses BigDecimal and BigInt in parsing.
    *
    *  @param m The text, in this case the response message text from websocket.
    *
    *  @return JSON or an exception if problems parsing, error holds the original String.
    */
  def parseAsJson(m: String): ErrorOr[Json] = {
    parser.parse(m).leftMap { pf =>
      new AppJsonParsingError("Error Parsing String to Json", m, pf)
    }
  }

  def parseAndDecode[A](m: String, decoder: Decoder[A]): Either[AppError, A] = {
    parseAsJson(m).flatMap(decode(_, decoder))
  }
  def parseAsJson(f: File): Either[AppException, Json] = {
    logger.info(s"Parsing FIle $f")
    new JawnParser().parseFile(f).leftMap { pf =>
      new AppException(s"Error Parsing File $f to Json", pf)
    }
  }

  /** Monoid/Semigroup for Circe Json Object so we can add them togeher. */
  implicit val jsonObjectMonoid: Monoid[JsonObject] = new Monoid[JsonObject] {
    def empty: JsonObject                                 = JsonObject.empty
    def combine(x: JsonObject, y: JsonObject): JsonObject = JsonObject.fromIterable(x.toVector |+| y.toVector)
  }

  /**
    *  {{{
    *    CirceUtils.decode(json.as[List[Foo]], json, "Decoding all Foo in the Bar")
    *  }}}
    * @param v
    * @param json
    * @param note
    * @tparam T
    * @return
    */
  def decode[T](v: Result[T], json: Json, note: String = "No Clues"): ErrorOr[T] = {
    v.leftMap { err: DecodingFailure =>
      new AppJsonDecodingError(json, err, note)
    }
  }

  def decode[T](json: Json, decoder: Decoder[T]): Either[AppJsonDecodingError, T] = {
    //val targs = typeOf[T] match { case TypeRef(_, _, args) => args }
    //val tmsg = s"type of $decoder has type arguments $targs"

    val decoderInfo = decoder.toString
    val msg         = s"Using Decoder $decoderInfo for Type"
    decoder.decodeJson(json).leftMap((e: DecodingFailure) => new AppJsonDecodingError(json, e, msg))
  }

  /** For now does top level pruning of null fields from JSON Object
    * Now recurses */
  def pruneNullFields(obj: JsonObject): JsonObject = {
    obj
      .filter {
        case (field, value) => !value.isNull
      }
      .mapValues { js: Json =>
        js.asObject match {
          case Some(obj) => pruneNullFields(obj).asJson
          case None      => js
        }
      }
      .asJsonObject

  }

  def json2jsonobject(json: Json): Either[AppError, JsonObject] = {
    json.asObject match {
      case None     => AppError("Converting JSON to Object wasn't an object", json).asLeft
      case Some(jo) => jo.asRight
    }
  }

  /**
    *   Deep descent through Json to find the first field by name.
    *   Returns error if not found, ignores multiple fields by returning only first.
    * @param name
    * @param json
    */
  def findFirstField(name: String, json: Json): Either[AppError, Json] = {
    json.findAllByKey(name) match {
      case head :: tail => head.asRight
      case _            => AppError(s"Field $name not found in deep traverse ", json.asJson).asLeft
    }
  }

  def findField(name: String, json: JsonObject): Either[AppError, Json] = {
    Either.fromOption(json(name), AppError(s"Field $name not found ", json.asJson))
  }

  def findObjectField(name: String, json: JsonObject): Either[AppError, JsonObject] = {
    findField(name, json).flatMap(json2jsonobject)
  }

  def findStringField(name: String, jobj: JsonObject): Either[AppError, String] = {
    findField(name, jobj).flatMap(json2string)
  }

  def json2array(json: Json): Either[AppError, List[Json]] = {
    Either.fromOption(json.asArray.map(_.toList), AppError("Expected JSON Array", json))
  }

  def json2string(json: Json): Either[AppError, String] = {
    Either.fromOption(json.asString, AppError("Expected JSON String", json))
  }

}

object CirceUtils extends CirceUtils
