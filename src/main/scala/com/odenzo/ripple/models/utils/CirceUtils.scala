package com.odenzo.ripple.models.utils

import java.io.File

import cats._
import cats.implicits._
import io.circe._
import io.circe.jawn.JawnParser
import io.circe.optics.JsonPath
import io.circe.syntax._
import monocle.Optional
import scribe.Logging

import com.odenzo.ripple.models.utils.caterrors.CatsTransformers.ErrorOr
import com.odenzo.ripple.models.utils.caterrors._

/**
  *  Traits for working with Circe Json / DOM
  */
trait CirceUtils extends Logging {

  /**
    * To avoid importing io.circe.syntax to use .asJson :-)
    *  Also allows explicitly passing in the encoder
    **/
  def encode[A](a: A)(implicit enc: Encoder[A]): Json = {
    enc.apply(a)
  }

  /** Easily decode wrapped in our Either AppError style. */
  def decode[A](json: Json, desc: String = "")(implicit decoder: Decoder[A]): Either[AppJsonDecodingError, A] = {
    json.as[A].leftMap { err =>
      AppJsonDecodingError(json, err)
    }
  }

  /** Easily decode wrapped in our Either AppError style. */
  def decodeObj[A](jobj: JsonObject, desc: String = "")(
      implicit decoder: Decoder[A]
  ): Either[AppJsonDecodingError, A] = {
    decode(jobj.asJson)
  }

  def extractFieldFromObject(jobj: JsonObject, fieldName: String): Either[OError, Json] = {
    Either.fromOption(jobj.apply(fieldName), ModelsLibError(s"Could not Find $fieldName in JSonObject "))
  }

  /**
    * Little utility for common case where an JsonObject just has "key": value
    * WHere value may be heterogenous?
    *
    * @param json
    */
  def extractAsKeyValueList(json: Json): ErrorOr[List[(String, Json)]] = {
    val obj: Either[OError, JsonObject]           = json.asObject.toRight(ModelsLibError("JSON Fragment was not a JSON Object"))
    val ans: Either[OError, List[(String, Json)]] = obj.map(_.toList)
    ans
  }

  /** Ripled doesn't like objects like { x=null } */
  val droppingNullsPrinter: Printer = Printer.spaces2.copy(dropNullValues = true)

  /** Converts json to formatted text dropping null JsonObject fields.
    *  @param json
    *  @return
    */
  def print(json: Json): String = json.printWith(droppingNullsPrinter)

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

  def parseAndDecode[A](m: String, decoder: Decoder[A]): Either[ModelsLibError, A] = {
    parseAsJson(m).flatMap(decode(_)(decoder))
  }
  def parseAsJson(f: File): Either[AppException, Json] = {
    logger.info(s"Parsing FIle $f")
    new JawnParser().parseFile(f).leftMap { pf =>
      new AppException(s"Error Parsing File $f to Json", pf)
    }
  }

  /** Monoid/Semigroup for Circe Json Object so we can add them togeher
    * Note that a + b kind of equals b + a -- the field order may differ but === still holds */
  implicit val jsonObjectMonoid: Monoid[JsonObject] = new Monoid[JsonObject] {
    def empty: JsonObject                                 = JsonObject.empty
    def combine(x: JsonObject, y: JsonObject): JsonObject = JsonObject.fromIterable(x.toVector |+| y.toVector)
  }

  /** For now does top level pruning of null fields from JSO, very innefficient */
  def pruneNullFields(obj: Json): ErrorOr[Json] = {
    parseAsJson(obj.asJson.printWith(droppingNullsPrinter))
  }

  /**
    *   Deep descent through Json to find the first field by name.
    *   Returns error if not found, ignores multiple fields by returning only first.
    * @param name
    * @param json
    */
  def findFirstField(name: String, json: Json): Either[ModelsLibError, Json] = {
    json.findAllByKey(name) match {
      case head :: tail => head.asRight
      case _            => ModelsLibError(s"Field $name not found in deep traverse ", json.asJson).asLeft
    }
  }

  /** Finds top level field in the supplied json object */
  def findField(name: String, json: JsonObject): Either[ModelsLibError, Json] = {
    findField(name, json.asJson)
  }

  def findObjectField(name: String, json: JsonObject): Either[ModelsLibError, JsonObject] = {
    findFieldAsObject(name, json.asJson)
  }

  def lensGetOpt[T](lens: Optional[Json, T])(applyTo: Json): Either[ModelsLibError, T] = {
    lens.getOption(applyTo).toRight(ModelsLibError(s"Path Failed ${lens.toString}", applyTo))
  }

  def hasField(name: String, json: Json): Boolean = {
    findField(name, json).isRight
  }

  def findField(name: String, json: Json): Either[ModelsLibError, Json] = {
    JsonPath.root.at(name).getOption(json).flatten.toRight(ModelsLibError(s"Field $name not found ", json.asJson))
  }

  def findFieldAsString(name: String, json: Json): Either[ModelsLibError, String] =
    findField(name, json).flatMap(json2string)

  def findFieldAsObject(name: String, json: Json): Either[ModelsLibError, JsonObject] =
    findField(name, json).flatMap(json2object)

  def findStringField(name: String, jobj: JsonObject): Either[ModelsLibError, String] = {
    findField(name, jobj).flatMap(json2string)
  }

  def json2array(json: Json): Either[ModelsLibError, List[Json]] = {
    Either.fromOption(json.asArray.map(_.toList), ModelsLibError("Expected JSON Array", json))
  }

  def json2string(json: Json): Either[ModelsLibError, String] = {
    Either.fromOption(json.asString, ModelsLibError("Expected JSON String", json))
  }

  def json2object(json: Json): Either[ModelsLibError, JsonObject] = {
    json.asObject match {
      case None     => ModelsLibError(" JSON to Object wasn't an object", json).asLeft
      case Some(jo) => jo.asRight
    }
  }
}

object CirceUtils extends CirceUtils
