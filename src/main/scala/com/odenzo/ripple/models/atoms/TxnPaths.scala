package com.odenzo.ripple.models.atoms

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder, Json}

/**
  * Note: Current use cases use auto-path finding so these are not well tested.
  * TODO: Details TxnPath integration testing with Scenarios
  */
trait TxnPaths {}

/**
  * Corresponds to one account trust line info in the response message of account_lines
  **/
/** ripple_path_find command result contents. */
case class RipplePathFindResult(
    alternatives: AlternativePaths,
    destination_account: AccountAddr,
    destination_currencies: List[Currency]
)

/** List of possible transaction paths per the ripple_path_find result sub-object
  *
  * @param source_amount This is a String or an object, not sure when is what
  * @param paths_computed
  */
case class AlternativePaths(paths_computed: List[List[PaymentPathStep]], source_amount: CurrencyAmount)

/** This may be a more general thing to move into models */
case class PaymentPath(hops: List[PaymentPathStep])

/**
  * Several different types of nodes. type = 1 is just account , type 48 is currency and issuer
  *     https://xrpl.org/paths.html
  * @param currency
  * @param issuer
  * @param zType Deprecated
  * @param type_hex        Deprectate
  */
case class PaymentPathStep(
    currency: Option[Currency],
    issuer: Option[AccountAddr],
    `type`: Option[Int] = None,
    type_hex: Option[String] = None
)

object PaymentPath {
  implicit val encoder: Encoder.AsObject[PaymentPath] = deriveEncoder[PaymentPath]
  implicit val decoder: Decoder[PaymentPath]          = deriveDecoder[PaymentPath]
}

object PaymentPathStep {
  implicit val encoder: Encoder.AsObject[PaymentPathStep] = deriveEncoder[PaymentPathStep]
  implicit val decoder: Decoder[PaymentPathStep]          = deriveDecoder[PaymentPathStep]
}

object RipplePathFindResult {
  implicit val encoder: Encoder.AsObject[RipplePathFindResult] = deriveEncoder[RipplePathFindResult]
  implicit val decoder: Decoder[RipplePathFindResult]          = deriveDecoder[RipplePathFindResult]
}

object AlternativePaths {
  implicit val encoder: Encoder.AsObject[AlternativePaths] = deriveEncoder[AlternativePaths]
  implicit val decoder: Decoder[AlternativePaths]          = deriveDecoder[AlternativePaths]
}
