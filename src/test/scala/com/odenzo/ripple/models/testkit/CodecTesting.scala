package com.odenzo.ripple.models.testkit

import java.nio.file.Path
import java.util

import cats.implicits._
import io.circe.Decoder.Result
import io.circe._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.{Assertion, EitherValues, Matchers}
import scribe.{Logging, Logger}

import com.odenzo.ripple.models.utils.{CirceUtils, ScribeConfig}
import com.odenzo.ripple.models.utils.caterrors.{AppError, AppException, AppJsonDecodingError}
import com.odenzo.ripple.models.atoms.LedgerHash

/**
  * test* methods have assertions the others are just helpers.
  */
trait CodecTesting extends AnyFunSuite with Matchers with EitherValues with Logging with CirceUtils {

  ScribeConfig.setTestLogging

  val dummyLedgerHash = LedgerHash(List.fill(20)("FF").mkString)

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

  def parseAsJObj(s: String): Either[AppError, JsonObject] = {
    CirceUtils.json2jsonobject(parse(s))
  }

  /** Parses Json to object and pack to json, returns  last two */
  def jsonRoundTrip[A](jsonStr: String)(implicit enc: Encoder[A], dec: Decoder[A]): Either[AppError, (A, Json, A)] = {
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
      case e: AppError  => myLog.error(s"Errors ${(e: AppError).show}")
      case e: Throwable => myLog.error(s"Throwable $msg\t=> $e ")
    }
    ee
  }

  def findFixtureFiles(dir: String, startingWith: String): Either[AppError, List[Path]] = {
    import collection.JavaConverters._ // Trying to keep scala 12 compatability with 13 now
    AppException.wrapPure("Finding Files") {
      // The Fixtures are now in the resource directory so load via resource
      // But non trivial to do in general case (from Jar and from IDE)
      import java.nio.file.{Files, Path, Paths}
      val url                      = this.getClass.getResource(s"/$dir")
      val path                     = Paths.get(url.toURI)
      val all: util.Iterator[Path] = Files.newDirectoryStream(path).iterator()
      val list: List[Path]         = all.asScala.toList
      // list.foreach((p: Path) => logger.info(s"[${p.getFileName()}] $p"))
      list
        .filterNot(p => p.getFileName.startsWith(startingWith))
        .sortBy(_.getFileName)

    }
  }

  def shouldSkipPath(p: Path, withNameContaining: Set[String]): Boolean = {
    val name: String = p.getFileName.toString
    val shouldSkip   = withNameContaining.exists(s => name.contains(s))
    shouldSkip
  }

}
