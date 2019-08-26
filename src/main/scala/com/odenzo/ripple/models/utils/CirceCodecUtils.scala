package com.odenzo.ripple.models.utils

import cats.implicits._
import io.circe.generic.extras.Configuration
import io.circe.{Json, JsonObject, ACursor}

import com.odenzo.ripple.models.atoms.Hash256
import com.odenzo.ripple.models.utils.caterrors.CatsTransformers.ErrorOr
import com.odenzo.ripple.models.utils.caterrors.{AppError, OError}

/** Going to start using Ripple Generic Extras. */
trait CirceCodecUtils {

  /**
    * Utility to rename a field in a JsonObject, typically used in encoders .mapJsonObject
    *
    * @param autoLedger
    * @param fieldName
    *
    * @return
    */
  def renameLedgerField(autoLedger: JsonObject, fieldName: String = "ledger"): JsonObject = {
    val oldKey = fieldName

    // Looks like Json.fold is reasonable way but not now.
    // Also, case Json.JString(v)  not working really. Are there unaaply somewhere.
    val ledgerVal: Option[(String, Json)] = autoLedger(oldKey).map {
      case json if json.isNumber => ("ledger_index", json)
      case json if json.isString =>
        val hashOrName: (String, Json) = json.asString match {
          case Some(ledger) if Hash256.isValidHash(ledger) => ("ledger_hash", json)
          case Some(assume_named_ledger)                   => ("ledger_index", json)
        }
        hashOrName

      case other => ("INVALID_LEDGER", Json.Null) // Not sure how to signal error yet
    }

    ledgerVal.map(field => field +: autoLedger.remove(oldKey)).getOrElse(autoLedger)

  }

  /**
    * Generic lifter being applied just to ledger default encoding for now. The default encoder will
    * make a Ledger subobject in Json, with differing fields based on the concrete instance Ledger subtype.
    * (i.e LedgerIndex, LedgerHash, LedgerId, LedgerName...)
    *
    * @param withLedger
    * @param field
    *
    * @return
    */
  def liftLedgerFields(withLedger: JsonObject, field: String = "ledger"): JsonObject = {

    val replaced: Option[JsonObject] = withLedger(field).map { subs =>
      // We should make this subs is a JsonObject to be pedantic and give good error messages
      val cursor = subs.hcursor
      // Lets make this more generic for fun
      val fieldsToLift: List[String] = cursor.keys.getOrElse(Vector.empty[String]).toList
      // val expectedFields: List[String] = "ledger_hash" :: "ledger_index" :: Nil

      // product should do the trick, or need OptionT?
      val fields: List[(String, Json)] = fieldsToLift.flatMap(name => cursor.downField(name).focus.map((name, _)))

      val newBase = withLedger.toList.filterNot(f => f._1.equals(field)) ::: fields
      JsonObject.fromIterable(newBase)
    }
    // If we found an object named field then we took all the subfield (possibly none) and shifted to field in new
    // JsonObject. If no field was found then we return the origiinal
    // TODO: Refactor (getOrElse {...} on top matches human description more
    replaced.getOrElse(withLedger)
  }

  //  /**
  //    * Used as a prepare() function to convert case class fields to uppercase first letter.
  //    * TODO: Can't figure our how to do it. Luckly circe-derivation to the rescue even though pre-release.
  //    */
  //  def upcaseFields(ac:ACursor) : ACursor = {
  //
  //    val fields = ac.fieldSet.getOrElse(Set.empty[String])
  //    fields.foreach { name =>
  //      ac.downField(name)
  //      val fieldVal = ac.focus.getOrElse(Json.Null)
  //      ac.delete
  //      // ac.set(json)   // Cant change the field name only value
  //
  //    }
  //    ac
  //
  //  }

  type KeyTransformer = String => String

  /** This is the transforms from io.circe.generic.extras */
  val snakeCaseTransformation: KeyTransformer =
    _.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z\\d])([A-Z])", "$1_$2").toLowerCase

  /** Capitalize a somewhat normal word */
  def capitalize(s: String): String = s.capitalize

  /** Transformation to capitalize the first letter only of a field name */
  val capitalizeTransformation: String => String = capitalize

  val decapitalizeTransformation: String => String = unCapitalize

  val capitalizeConfiguration: Configuration =
    Configuration.default.copy(transformMemberNames = capitalizeTransformation)

  val capitalizeExcept: Configuration = Configuration.default.copy(transformMemberNames = capitalizeExceptFn)

  def capitalizeExceptFn(s: String): String = {
    s match {
      case "hash"  => "hash"
      case "index" => "index"
      case other   => capitalize(other)
    }
  }

  def unCapitalize(s: String): String = {
    if (toString == null) null
    else if (toString.length == 0) s
    else if (toString.charAt(0).isLower) s
    else {
      val chars = toString.toCharArray
      chars(0) = chars(0).toLower
      new String(chars)
    }
  }

  //private val renamer: (String) => String = upcaseExcept("hash" :: Nil)

  val whitelist: Set[String] = Set("hash", "id", "command", "secret", "tx_json")

  /** Typically used (slowly) for encoders. Possibly better to use derived or generic-extras
    * Note that it has a white-list of fields not to upcase (e.g. hash)
    **/
  def upcaseFields(obj: JsonObject): JsonObject = {
    // Could optimize this a bit by checking if key.head.isUpper or something I bet.
    // And also case (key,json) if !whitelist.contains(key)
    // Also design these generic and maybe so we can compose on fields
    val upcasedFirst = obj.toList.map {
      case (key: String, json: Json) if !whitelist.contains(key) => (key.capitalize, json)
      case other                                                 => other
    }
    JsonObject.fromIterable(upcasedFirst)
  }

  /** Idea here is to partially apply this with any (oldName,newName) entries you want. If name not found then no
    * changes to name.
    * For instance, to capitalize all and change some_oddball to SomeOddball
    * {{{
    *   val myMap = Map[String,String] ( "SomeOddball" -> "some_oddball")
    *   val customerOnly = customerNameTransformer(myMap)
    *   val composeStuff = capitalize.compose(customerOnly) // or x compose y style
    *   val moreReadbleFn = customerOnly.andThen(capitalize)
    * }}}
    *
    * @param map
    * @param name
    *
    * @return
    */
  def customNameTransformer(map: Map[String, String], name: String): String = map.getOrElse(name, name)

  def upcaseExcept(these: List[String]): (String) => String = { (key: String) =>
    val newKey = if (!these.contains(key)) key.capitalize else key
    //logger.debug(s"Converted $key -> $newKey")
    newKey
  }

  /** Caution that this must be done AFTER any general transaction of names for top level */
  def liftJsonObject(obj: JsonObject, field: String): JsonObject = {
    // Relies RippleTransaction being encoded as JsonObject
    obj(field).flatMap(_.asObject) match {
      case None            => obj
      case Some(objToLift) => JsonObject.fromIterable(obj.remove(field).toList ::: objToLift.toList)
    }
  }

  /**
    * {{{
    *   changeFieldName("tipe", "type")(myEncodedObject)
    * }}}
    * @param name
    * @param newName
    * @param in
    * @return
    */
  def changeFieldName(name: String, newName: String)(in: JsonObject): JsonObject = {
    val updated: Option[JsonObject] = in(name)
      .map(oldVal => in.add(newName, oldVal))
      .map(jo => jo.remove(name))
    updated.getOrElse(in)
  }

  /** *
    * {{{
    *     val changer = fieldNameChangeEx("oldName","newName")
    *     Decoder[A].prepare(prepareByMungingJsonObject(changer))
    * }}}
    *
    * @param fn
    * @param in
    *
    * @return
    */
  def prepareByMungingJsonObject(fn: JsonObject => JsonObject)(in: ACursor): ACursor = {
    in.withFocus(json => json.mapObject(jobj => fn(jobj)))
  }

  def extractFieldFromObject(jobj: JsonObject, fieldName: String): Either[OError, Json] = {
    Either.fromOption(jobj.apply(fieldName), AppError(s"Could not Find $fieldName in JSonObject "))
  }

  /**
    * Little utility for common case where an JsonObject just has "key": value
    * WHere value may be heterogenous?
    *
    * @param json
    */
  def extractAsKeyValueList(json: Json): ErrorOr[List[(String, Json)]] = {
    val obj: Either[OError, JsonObject]           = json.asObject.toRight(AppError("JSON Fragment was not a JSON Object"))
    val ans: Either[OError, List[(String, Json)]] = obj.map(_.toList)
    ans
  }

  /**
    * Parses the list of json key  value pairs until it hits first error (not-accumulating parsing).
    *
    * @param json
    * @param fn
    * @tparam T
    *
    * @return
    */
  def parseKeyValuesList[T](json: Json, fn: (String, Json) => Either[AppError, T]): ErrorOr[List[T]] = {
    val kvs: ErrorOr[List[(String, Json)]] = extractAsKeyValueList(json)
    kvs.flatMap { theList =>
      theList.traverse((tup: (String, Json)) => fn(tup._1, tup._2))
    }
  }
}

object CirceCodecUtils extends CirceCodecUtils
