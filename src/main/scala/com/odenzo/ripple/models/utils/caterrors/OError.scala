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
trait AppError extends Throwable { // Note this is not sealed and some shit defined elsewhere in the pasta bowl
  def msg: String

  def cause: Option[AppError]

  def asErrorOr[A]: ErrorOr[A] = this.asLeft[A]
}

object ShowHack {
  implicit val showBaseError: Show[AppError] = Show.show[AppError] {
    case err: AppJsonError          => err.show
    case err: AppErrorRestCall      => err.show
    case err: AppJsonDecodingError  => err.show
    case err: AppRippleGenericError => err.show
    case err: AppRippleEngineError  => err.show
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
object AppError {

  lazy implicit val show: Show[AppError] = Show.show[AppError] { failure: AppError =>
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

  def apply(json: Json): AppError = AppJsonError("Invalid Json", json)

  def apply(m: String, json: Json): AppError = AppJsonError(m, json)

  def apply(m: String, json: Json, e: AppError): AppError = AppJsonError(m, json, Some(e))

  def apply(m: String): OError = OError(m, None)

  def apply(m: String, ex: Throwable): AppException = new AppException(m, ex)

  def required[T](v: Option[T], msg: String = "Required value not present"): Either[AppError, T] = {
    Either.fromOption(v, OError(msg))
  }

  /**
    * Produces a list of strings summarizing the error, going down the stack.
    */
  def summary(err: AppError): List[String] = {
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
class OError(val msg: String = "No Message", val cause: Option[AppError] = None) extends AppError

object OError {

  /** Ignore the compimle error in IntelliJ, but not the crappy coding needs redo */
  lazy implicit val showOError: Show[OError] = Show.show[OError] { (failure: OError) =>
    val top = s"OError -> ${failure.msg}"
    val sub = failure.cause.map((x: AppError) => x.show)
    top + sub
  }

  def catchNonFatal[A](f: => A): ErrorOr[A] = {
    val ex: Either[Throwable, A]     = Either.catchNonFatal(f)
    val res: Either[AppException, A] = ex.leftMap(e => new AppException("Wrapped Exception", e))
    res
  }

  def apply(m: String) = new OError(m, None)

  def apply(m: String, e: AppError) = new OError(m, Some(e))

  def apply(m: String, e: Option[AppError] = None) = new OError(m, e)

}

/**
  * For use in Request/Response scenarios, or more generally when  a:Json => b:Json => transform(b) => answer
  * This can be used.
  * Prefer to use ORestCall when have source and response json at one place.
  */
class AppJsonError(val msg: String, val json: Json, val cause: Option[AppError] = None) extends AppError {}

class AppJsonParsingError(val msg: String, val raw: String, val parser: ParsingFailure) extends AppError {
  val cause: Option[AppError] = new AppException(parser.message, parser).some
}

/**
  * A Ripple protocol error, parameterized on a payload. Typically a RippleGenericResponse
  * This error should be moved into ComKit or rippled-wsmodels
  *
  * @param msg Explanatory message
  * @param obj A model object expected as a result of decoding the Ripple response.
  * @tparam T Type of grs
  */
class AppRippleError[T](val msg: String, val obj: T) extends AppError {
  val cause: Option[AppError] = None
}

object AppRippleError {

  implicit val show: Show[AppRippleError[_]] = Show.show { failure =>
    val basic =
      s""" AppRippleError[-] shower
      | Error:   ${failure.msg}
      | Object: ${failure.obj}
      | Cause is OverRidden as None.
    """
    val sub = failure.obj match {
      case v: AppError => v.show
      case other       => other.getClass().toGenericString()
    }
    basic + sub

  }
}

class AppRippleGenericError(val rr: JsonReqRes, val rippleGenericError: RippleGenericError) extends AppError {
  def msg: String = "Ripple Returned a Generic Error Result"

  def cause: Option[AppError] = None
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
class AppRippleEngineError(val submitRR: JsonReqRes, val rs: RippleTxnRs) extends AppError {
  def msg: String = "Submission of a Transaction Failed on Ripple"

  def cause: Option[AppError] = None
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
    val cause: Option[AppError] = None
) extends AppError

/**
  * Represents a error in Circe Json decoding (Json => Model)
  *
  * @param json JSON that was input, generally the complete document in scope
  * @param err  The decoding failure from Circe.
  * @param note Informational message to enhance the exception, provides context.
  */
case class AppJsonDecodingError(val json: Json, val err: DecodingFailure, val note: String = "") extends AppError {
  val msg: String             = note + ":" + err.message
  val base: String            = s"\n OR: ${err.show}"
  val cause: Option[AppError] = None

}

object AppJsonDecodingError {
  implicit val show: Show[AppJsonDecodingError] = Show.show[AppJsonDecodingError] { failure: AppJsonDecodingError =>
    val base          = s"ODecodingError -->  ${failure.err.show} \n\t\t On JSON: ${failure.json.spaces2}"
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
         .map((x: AppError) => x.show)
         .getOrElse("<Nothing>")}""".stripMargin
  }

  def apply(msg: String, json: Json): AppJsonError = new AppJsonError(msg, json)

  def apply(msg: String, json: Json, cause: AppError): AppJsonError = new AppJsonError(msg, json, Some(cause))

  def apply(msg: String, json: Json, cause: Option[AppError] = None): AppJsonError = {
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
class AppException(val msg: String = "Wrapping Root Exception", val err: Throwable) extends AppError {
  val cause: Option[AppError] = Option.empty
}

object AppException extends StackUtils {

  implicit val show: Show[AppException] = Show.show[AppException] { errorException =>
    s"OErrorException -->  ${errorException.msg} \n\t\t " +
      s"Exception Message: ${errorException.err}\n\t\t" +
      s"Exception Class: \t${errorException.err.getClass}\n\t\t" +
      s"StackTrace As String: ${stackAsString(errorException.err)}"

  }

  def wrap[A](msg: String)(fn: => Either[AppError, A]): Either[AppError, A] = {
    Try {
      fn
    } match {
      case Success(v: Either[AppError, A]) => v
      case Failure(exception)              => AppException(msg, exception).asLeft
    }
  }

  def wrapPure[A](msg: String)(fn: => A): Either[AppError, A] = {
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
