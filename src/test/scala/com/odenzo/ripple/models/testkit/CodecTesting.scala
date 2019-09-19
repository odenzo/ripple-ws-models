package com.odenzo.ripple.models.testkit

import java.net.URL
import java.nio.file.Path
import java.util
import scala.io.{BufferedSource, Source}

import cats.implicits._
import io.circe.Decoder.Result
import io.circe._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.{Assertion, EitherValues, Matchers}
import scribe.{Logger, Logging}

import com.odenzo.ripple.models.utils.{CirceUtils, ScribeConfig}
import com.odenzo.ripple.models.utils.caterrors.{AppException, AppJsonDecodingError, ModelsLibError}
import com.odenzo.ripple.models.atoms.LedgerHash

/**
  * test* methods have assertions the others are just helpers.
  */
trait CodecTesting extends AnyFunSuite with Matchers with EitherValues with Logging with CirceUtils {

  ScribeConfig.setTestLogging

  val dummyLedgerHash = LedgerHash(List.fill(20)("FF").mkString)

  def loadJsonResource(path: String): Either[ModelsLibError, Json] = {
    AppException.wrap(s"Getting Resource $path") {
      val resource: URL          = getClass.getResource(path)
      val source: BufferedSource = Source.fromURL(resource)
      val data: String           = source.getLines().mkString("\n")
      CirceUtils.parseAsJson(data)
    }
  }

  /**
    * Utility to parse a JSON string. Will fail test if cannot parse.
    * @param s
    * @return
    */
  def parse(s: String): Json = {
    io.circe.parser.parse(s) match {
      case Left(err)   => fail(s"Could not parse json $err from String:\n$s")
      case Right(json) => json
    }
  }

  def parseAsJObj(s: String): Either[ModelsLibError, JsonObject] = {
    CirceUtils.json2jsonobject(parse(s))
  }

  /** Parses Json to object and pack to json, returns  last two */
  def jsonRoundTrip[A](
      jsonStr: String
  )(implicit enc: Encoder[A], dec: Decoder[A]): Either[ModelsLibError, (A, Json, A)] = {
    for {
      json <- parseAsJson(jsonStr)
      obj  <- decode(json)
      jsonBack = encode(obj)
      _        = logger.debug(s"Object ${pprint.apply(obj)}")
      _        = if (jsonBack != json) logger.warn(s"JSON Mistmatch:\n ${json.spaces4} \n =!= \n ${jsonBack.spaces4}")
    } yield (obj, jsonBack, obj)
  }

  def objRoundTrip[A](a: A)(implicit codec: Codec.AsObject[A]): Either[AppJsonDecodingError, (A, Json)] = {
    for {
      json <- encode(a).asRight
      obj  <- decode(json)
    } yield (obj, json)
  }

  /** For the things that have both encoders and decoders (not rq encoder , rs decoder) */
  def testRoundtrip[T](o: T, encoder: Encoder[T], decoder: Decoder[T]): Assertion = {
    val json: Json = testEncoding(o, encoder)
    val o2: T      = testDecoding(json, decoder)
    o shouldEqual o2

  }

  def testDecoding[T](json: Json, decoder: Decoder[T]): T = {
    decoder.decodeJson(json) match {
      case Left(err) => fail(s"Could not decode json $err \n from the JSON:\n${json.spaces2}")
      case Right(ok) => logger.debug(s"Decoded: $ok"); ok
    }
  }

  /** Parses the input string and applies the decoding. Logging errors and asserting success */
  def testDecoding[T](s: String, decoder: Decoder[T]): T = {
    testDecoding(parse(s), decoder)
  }

  /** This tries to apply the encoder, which can never fail except exceptions which we let go */
  def testEncoding[T](t: T, encoder: Encoder[T]): Json = {
    val res: Json = encoder.apply(t)
    logger.debug(s"Encoding $t (${t.getClass} of ${encoder} Yields:\n${res.spaces2}")
    res
  }

  def logResult[A](a: Result[A], msg: String = "Circe Decoding"): Unit = {
    a match {
      case Left(err) => logger.warn(s"$msg Error: $err")
      case Right(v)  => logger.debug(s"$msg Parsing OK   : $v")
    }
  }

  def testCompleted[T](ee: Either[Throwable, T], msg: String = "Error: ", myLog: Logger = logger): T = {
    logIfError(ee, msg, myLog)
    ee match {
      case Right(v) => v
      case Left(e)  => fail(s"getOrLog error ${e.getMessage}")
    }
  }

  def logIfError[A <: Throwable, T](ee: Either[A, T], msg: String = "Error: ", myLog: Logger = logger): Either[A, T] = {
    ee.leftMap {
      case e: ModelsLibError => myLog.error(s"Errors ${(e: ModelsLibError).show}")
      case e: Throwable      => myLog.error(s"Throwable $msg\t=> $e ")
    }
    ee
  }

  def loadFixture(dir: String, file: String): Either[ModelsLibError, Json] = {
    val json: Either[ModelsLibError, Json] = this.loadJsonResource(s"$dir/$file")
    json
  }

  def shouldSkipPath(p: Path, withNameContaining: Set[String]): Boolean = {
    val name: String = p.getFileName.toString
    val shouldSkip   = withNameContaining.exists(s => name.contains(s))
    shouldSkip
  }

}
