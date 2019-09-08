package com.odenzo.ripple.models.atoms

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

trait TxnPaths

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
  */
case class PaymentPathStep(
    currency: Option[Currency],
    issuer: Option[AccountAddr],
    `type`: Option[Int] = None,
    type_hex: Option[String] = None
)

object PaymentPath {
  implicit val config: Configuration              = Configuration.default
  implicit val codec: Codec.AsObject[PaymentPath] = deriveConfiguredCodec[PaymentPath]

}

object PaymentPathStep {
  implicit val config: Configuration                  = Configuration.default
  implicit val codec: Codec.AsObject[PaymentPathStep] = deriveConfiguredCodec[PaymentPathStep]
}

object RipplePathFindResult {
  implicit val config: Configuration                       = Configuration.default
  implicit val codec: Codec.AsObject[RipplePathFindResult] = deriveConfiguredCodec[RipplePathFindResult]
}

object AlternativePaths {
  implicit val config: Configuration                   = Configuration.default
  implicit val codec: Codec.AsObject[AlternativePaths] = deriveConfiguredCodec[AlternativePaths]
}
