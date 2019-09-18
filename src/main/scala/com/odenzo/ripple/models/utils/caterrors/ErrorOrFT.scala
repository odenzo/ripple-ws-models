package com.odenzo.ripple.models.utils.caterrors

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.util.control.NonFatal
import scala.util.{Success, Failure, Try}

import cats.data._
import cats.implicits._
import scribe.Logging

import com.odenzo.ripple.models.utils.caterrors.CatsTransformers.{ErrorOr, ErrorOrFT}

/** This is meant to be a standard (cross project even way to use Cats validation and results.
  *  Starting to think Circe Result style is best, just have an exception with helpers to create.
  *  Maybe the full Json is also nice to have, perhaps a InvalidJsonContextException to help with it.
  *  Look at io.circe.Error example
  */
object CatsTransformers extends Logging {

  type ErrorOr[A] = Either[ModelsLibError, A]

  /** Shorthand for a Future ErrorOr ... prefer to standardize on ErrorOrFT instead */
  type ErrorOrF[A] = Future[ErrorOr[A]]

  /** Uses Cats EitherT monad transformat to wrap Future[Either[OError,A]
    *  Would like to make it so this is the inferred type by intellij
    */
  type ErrorOrFT[A] = EitherT[Future, ModelsLibError, A]

  // See Advanced Scala with Cats.
  // type ErrorOrOptFT[A] = OptionT[ErrorOrFT]

  /** Useful Cats EitherT when not using the standard OError error type */
  type FutureEither[A, B] = EitherT[Future, A, B]

}

trait CatsTransformerOps {
  def notImplemented(implicit ec: ExecutionContext): ErrorOrFT[Nothing] = {
    fromEither(OError("Not Implemented").asLeft)
  }

  def pure[B](b: B)(implicit ec: ExecutionContext): ErrorOrFT[B] = {
    EitherT.pure[Future, ModelsLibError](b)
  }

  def fromEither[B](b: Either[ModelsLibError, B])(implicit ec: ExecutionContext): ErrorOrFT[B] = {
    EitherT.fromEither[Future](b)
  }

  def fromError[B](e: ModelsLibError)(implicit ec: ExecutionContext): ErrorOrFT[B] = {
    fromEither[B](e.asLeft[B])
  }

  def fromOpt[B](
      b: Option[B],
      msg: String = "Optional value not present"
  )(implicit ec: ExecutionContext): ErrorOrFT[B] = {
    b match {
      case Some(v) => pure[B](v)
      case None    => fromError(OError(msg))
    }
  }

  /** Takes a future and catches all non fatal exceptions, returning ErrorFT with the exception wrapped in OError
    * So if future has an non-fatal exception it returns Succesfully with a left Either containing the exception
    */
  def fromFuture[B](b: Future[B])(implicit ec: ExecutionContext): ErrorOrFT[B] = {

    val caught: Either[Throwable, Future[B]] = Either.catchNonFatal(b)
    val massaged: Either[Future[AppException], Future[B]] = {
      caught.leftMap { thrown =>
        val ex                            = new AppException(err = thrown)
        val wrapped: Future[AppException] = Future.successful(ex)
        wrapped
      }
    }
    val a2: Future[Either[ModelsLibError, B]]      = massaged.bisequence
    val answer: EitherT[Future, ModelsLibError, B] = EitherT(a2)
    answer
  }

}

object ErrorOr {
  def ok[B](b: B): ErrorOr[B]                  = b.asRight[ModelsLibError]: ErrorOr[B]
  def failed[B](a: ModelsLibError): ErrorOr[B] = a.asLeft
}

object ErrorOrFT extends CatsTransformerOps {
  import scala.concurrent.ExecutionContext.Implicits.global

  def NOT_IMPLEMENTED[T]: ErrorOrFT[T] = EitherT.fromEither[Future](ModelsLibError.NOT_IMPLEMENTED_ERROR)

  def apply[T](v: Future[Either[ModelsLibError, T]]): ErrorOrFT[T] = EitherT(v)

  def sync[T](eoft: ErrorOrFT[T], duration: Duration)(implicit ec: ExecutionContext): ErrorOr[T] = {
    Try {
      Await.result(eoft.value, duration)
    } match {
      case Success(s: ErrorOr[T]) => s
      case Failure(NonFatal(ex: Throwable)) =>
        new AppException(s"ErrorOrFT Failed Syncing After $duration", ex)
          .asLeft[T]
    }

  }

}
