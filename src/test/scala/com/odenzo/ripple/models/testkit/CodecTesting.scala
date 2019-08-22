package com.odenzo.ripple.models.testkit

import cats.implicits._
import io.circe.Decoder.Result
import io.circe.{Json, Encoder, Decoder}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.{Assertion, EitherValues, Matchers}
import scribe.{Logging, Logger}

import com.odenzo.ripple.models.utils.{CirceUtils, ScribeConfig}
import com.odenzo.ripple.models.utils.caterrors.AppError

/**
  * Slowly rebuilding a suite of unit tests for the models, which don't require calling Ripple.
  * Not sure this is really worth the effort but the AnyVal classes fight with import optimization
  */
trait CodecTesting extends AnyFunSuite with Matchers with EitherValues with Logging with CirceUtils {

  ScribeConfig.setTestLogging

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

  def getOrLog[T](ee: Either[Throwable, T], msg: String = "Error: ", myLog: Logger = logger): T = {
    ee.leftMap {
      case e: AppError  => myLog.error(s"Errors ${e.show}")
      case e: Throwable => myLog.error(s"Throwable $msg\t=> $e ")
    }

    ee match {
      case Right(v) => v
      case Left(e)  => fail(s"getOrLog error ${e.getMessage}")
    }
  }
}
