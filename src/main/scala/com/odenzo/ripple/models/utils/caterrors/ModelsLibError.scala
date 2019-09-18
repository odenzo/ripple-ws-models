package com.odenzo.ripple.models.utils.caterrors

import scala.util.{Success, Failure, Try}

import cats._
import cats.data._
import cats.implicits._
import io.circe.syntax._
import io.circe.Decoder.Result
import io.circe.{ParsingFailure, Json, DecodingFailure}

import com.odenzo.ripple.models.support.{JsonReqRes, RippleGenericError}
import com.odenzo.ripple.models.utils.caterrors.CatsTransformers.ErrorOr
import com.odenzo.ripple.models.wireprotocol.RippleTxnRs

/**
  * Base class that all errors (including OError) must extends directly or indirectly.
  * Not quite ready to move to case classes.
  */
trait ModelsLibError extends Throwable { // Note this is not sealed and some shit defined elsewhere in the pasta bowl
  def msg: String

  def cause: Option[ModelsLibError]

  def asErrorOr[A]: ErrorOr[A] = this.asLeft[A]
}

object ShowHack {
  implicit val showBaseError: Show[ModelsLibError] = Show.show[ModelsLibError] {
    case err: AppJsonError          => err.show
    case err: AppErrorRestCall      => err.show
    case err: AppJsonDecodingError  => err.show
    case err: AppRippleGenericError => err.show
    case err: AppRippleEngineError  => err.show
    case err: AppRippleError        => err.show
    case err: AppException          => err.show
    case err: DecodingFailure       => err.toString()
    case err: OError                => "\n --- " + err.show
    case other                      => "\n ****** Unknown Error" + other.toString
  }
}

/**
  * Base Error is never instanciated, but the apply is up here as convenience
  * and delegates down. These will go away soon.
  */
object ModelsLibError {

  lazy implicit val show: Show[ModelsLibError] = Show.show[ModelsLibError] { failure: ModelsLibError =>
    val base = ShowHack.showBaseError.show(failure)
    val nested = failure.cause
      .map(sub => ShowHack.showBaseError.show(sub))
      .getOrElse("No Nested Cause")

    "AppErrorShow: " + base + "\n\t" + nested
  }

  /** Ignore the compile error in IntelliJ due to recursive show definition
    * Also not the generic restriction/assumption for now that Throwable is bottom oh error stack.
    * */
  lazy implicit val showThrowables: Show[Throwable] = Show.show[Throwable] { t =>
    s"Showing Throwable ${t.getMessage}" + Option(t.getCause).map((v: Throwable) => v.toString).getOrElse("<No Cause>")
  }
  val NOT_IMPLEMENTED_ERROR: ErrorOr[Nothing] = Left(OError("Not Implemented"))

  def apply(json: Json): ModelsLibError = AppJsonError("Invalid Json", json)

  def apply(m: String, json: Json): ModelsLibError = AppJsonError(m, json)

  def apply(m: String, json: Json, e: ModelsLibError): ModelsLibError = AppJsonError(m, json, Some(e))

  def apply(m: String): OError = OError(m, None)

  def apply(m: String, ex: Throwable): AppException = new AppException(m, ex)

  def required[T](v: Option[T], msg: String = "Required value not present"): Either[ModelsLibError, T] = {
    Either.fromOption(v, OError(msg))
  }

  /**
    * Produces a list of strings summarizing the error, going down the stack.
    */
  def summary(err: ModelsLibError): List[String] = {
    err.cause match {
      case None         => err.msg :: Nil
      case Some(nested) => err.msg :: summary(nested)
    }
  }

}

/** SHould move to sealed module level hierarchies? */
/** The general error handling class, use instead of Throwables in EitherT etc .
  * Preferred method is to use the helper functions in StdContext.
  * These claseses may be made private in the future.
  */
class OError(val msg: String = "No Message", val cause: Option[ModelsLibError] = None) extends ModelsLibError

object OError {

  /** Ignore the compimle error in IntelliJ, but not the crappy coding needs redo */
  lazy implicit val showOError: Show[OError] = Show.show[OError] { (failure: OError) =>
    val top = s"OError -> ${failure.msg}"
    val sub = failure.cause.map((x: ModelsLibError) => x.show)
    top + sub
  }

  def catchNonFatal[A](f: => A): ErrorOr[A] = {
    val ex: Either[Throwable, A]     = Either.catchNonFatal(f)
    val res: Either[AppException, A] = ex.leftMap(e => new AppException("Wrapped Exception", e))
    res
  }

  def apply(m: String) = new OError(m, None)

  def apply(m: String, e: ModelsLibError) = new OError(m, Some(e))

  def apply(m: String, e: Option[ModelsLibError] = None) = new OError(m, e)

}

/**
  * For use in Request/Response scenarios, or more generally when  a:Json => b:Json => transform(b) => answer
  * This can be used.
  * Prefer to use ORestCall when have source and response json at one place.
  */
class AppJsonError(val msg: String, val json: Json, val cause: Option[ModelsLibError] = None) extends ModelsLibError {}

class AppJsonParsingError(val msg: String, val raw: String, val parser: ParsingFailure) extends ModelsLibError {
  val cause: Option[ModelsLibError] = new AppException(parser.message, parser).some
}

/**
  * This is a parent to handle errors generated while processing a Ripple call once we
  *  have the base JsonReqRes (e.g. Decoding, command failures, whatever)
  */
case class AppRippleError(rr: JsonReqRes, cause: Option[ModelsLibError]) extends ModelsLibError {
  val msg = "Processing Ripple Request Response"
}

object AppRippleError {

  implicit val show: Show[AppRippleError] = Show.show { failure =>
    s"""
       | AppRippleError:
       | Json Context:\n ${failure.rr.show}
       | Cause: ${failure.cause.show}
    """.stripMargin

  }
}

class AppRippleGenericError(val rr: JsonReqRes, val rippleGenericError: RippleGenericError) extends ModelsLibError {
  def msg: String = "Ripple Returned a Generic Error Result"

  def cause: Option[ModelsLibError] = None
}

object AppRippleGenericError {

  implicit val show: Show[AppRippleGenericError] = Show.show[AppRippleGenericError] { err =>
    s"""
       | Ripple Generic Error:
       | Request:
       | ${err.rr.rq.asJson.spaces4}
       | Result:
       | ${err.rr.rs.asJson.spaces4}
       |  Decoded: ${err.rippleGenericError}
   """.stripMargin
  }
}

/** This can happen after submitting a signed request, we keep all the info for debugging */
class AppRippleEngineError(val submitRR: JsonReqRes, val rs: RippleTxnRs) extends ModelsLibError {
  def msg: String = "Submission of a Transaction Failed on Ripple"

  def cause: Option[ModelsLibError] = None
}

object AppRippleEngineError {

  implicit val show: Show[AppRippleEngineError] = Show.show[AppRippleEngineError] { err =>
    s"""
       | Ripple Engine Error:
       | Request:
       | ${err.submitRR.rq.asJson.spaces4}
       | Result:
       | ${err.submitRR.rs.asJson.spaces4}

   """.stripMargin
  }
}

/**
  * Special OError for JSON request / response errors which are generally wrapped.
  * Could just make a Throwable to suit but...
  */
class AppErrorRestCall(
    val msg: String,
    val rawRq: Option[Json],
    val rawRs: Option[Json],
    val cause: Option[ModelsLibError] = None
) extends ModelsLibError

/**
  * Represents a error in Circe Json decoding (Json => Model)
  *
  * @param json JSON that was input, generally the complete document in scope
  * @param err  The decoding failure from Circe.
  * @param note Informational message to enhance the exception, provides context.
  */
case class AppJsonDecodingError(val json: Json, val err: DecodingFailure, val note: String = "")
    extends ModelsLibError {
  val msg: String                   = note + ":" + err.message
  val base: String                  = s"\n OR: ${err.show}"
  val cause: Option[ModelsLibError] = None

}

object AppJsonDecodingError {
  implicit val show: Show[AppJsonDecodingError] = Show.show[AppJsonDecodingError] { failure: AppJsonDecodingError =>
    val base          = s"OJsoneaDecodingError -->  ${failure.err.show} \n\t\t On JSON: ${failure.json.spaces2}"
    val stackAsString = "\n\nStack as String: " + StackUtils.stackAsString(failure.err)
    // val stackTrace = "\n\nStack Trace " + StackUtils.printStackTrace(failure.err)
    base + "\n DecodingFailure History: " + failure.err.history + stackAsString
  }

  /**
    * Wrap the Decoding error if there was one, and return as Either
    */
  def wrapResult[T](v: Result[T], json: Json, note: String = "No Clues"): ErrorOr[T] = {
    v.leftMap { err: DecodingFailure =>
      new AppJsonDecodingError(json, err, note)
    }
  }

}

object AppJsonError {

  lazy implicit val show: Show[AppJsonError] = Show.show { failure =>
    s"""
       | OErrorJson:
       | Error:\t ${failure.msg}
       | JSON :\t  ${failure.json.spaces2}
       | CAUSE:\t\n ${failure.cause
         .map((x: ModelsLibError) => x.show)
         .getOrElse("<Nothing>")}""".stripMargin
  }

  def apply(msg: String, json: Json): AppJsonError = new AppJsonError(msg, json)

  def apply(msg: String, json: Json, cause: ModelsLibError): AppJsonError = new AppJsonError(msg, json, Some(cause))

  def apply(msg: String, json: Json, cause: Option[ModelsLibError] = None): AppJsonError = {
    new AppJsonError(msg, json, cause)
  }
}

object AppErrorRestCall {

  implicit val show: Show[AppErrorRestCall] = Show.show { failure =>
    s"""
       | Error:   ${failure.msg}
       | JSON RQ: ${failure.rawRq.getOrElse(Json.Null).spaces2}
       | JSON RS: ${failure.rawRs.getOrElse(Json.Null).spaces2}
       | CAUSE:   ${failure.cause
         .map(v => v.show)
         .getOrElse("<Nothing>")}""".stripMargin
  }
}

/** This should be terminal node only */
class AppException(val msg: String = "Wrapping Root Exception", val err: Throwable) extends ModelsLibError {
  val cause: Option[ModelsLibError] = Option.empty
}

object AppException extends StackUtils {

  implicit val show: Show[AppException] = Show.show[AppException] { errorException =>
    s"AppException -->  ${errorException.msg} \n\t\t " +
      s"Exception Message: ${errorException.err}\n\t\t" +
      s"Exception Class: \t${errorException.err.getClass}\n\t\t" +
      s"StackTrace As String: ${stackAsString(errorException.err)}"

  }

  def wrap[A](msg: String)(fn: => Either[ModelsLibError, A]): Either[ModelsLibError, A] = {
    Try {
      fn
    } match {
      case Success(v: Either[ModelsLibError, A]) => v
      case Failure(exception)                    => AppException(msg, exception).asLeft
    }
  }

  def wrapPure[A](msg: String)(fn: => A): Either[ModelsLibError, A] = {
    Try {
      fn
    } match {
      case Success(v)         => v.asRight
      case Failure(exception) => AppException(msg, exception).asLeft
    }
  }

  def apply(msg: String): AppException = new AppException(err = new RuntimeException(msg))

  def apply(ex: Throwable): AppException = new AppException(err = ex)

  def apply(msg: String, err: Throwable) = new AppException(msg, err)
}
