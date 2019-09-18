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
  */
case class AlternativePaths(apaths: List[AlternativePath])

case class AlternativePath(
    paths_computed: List[PaymentPath],
    paths_canonical: List[PaymentPath],
    source_amount: CurrencyAmount
)

/** This may be a more general thing to move into models */
case class PaymentPath(hops: List[PaymentPathStep])

object PaymentPath {
  // hops field doesn't exixt. PaymentPath is a array of objects.
  // Each object is a JsonObject corresponing to PaymentPathStep
  implicit val config: Configuration     = Configuration.default
  implicit val codec: Codec[PaymentPath] = deriveUnwrappedCodec[PaymentPath]

}

/**
  * Several different types of nodes. type = 1 is just account , type 48 is currency and issuer
  *     https://xrpl.org/paths.html
  * type and type_hex are now deprecated so skipping them
  */
case class PaymentPathStep(
    account: Option[AccountAddr],
    currency: Option[Currency],
    issuer: Option[AccountAddr]
)

object PaymentPathStep {
  implicit val config: Configuration                  = Configuration.default
  implicit val codec: Codec.AsObject[PaymentPathStep] = deriveConfiguredCodec[PaymentPathStep]
}

object RipplePathFindResult {
  implicit val config: Configuration                       = Configuration.default
  implicit val codec: Codec.AsObject[RipplePathFindResult] = deriveConfiguredCodec[RipplePathFindResult]
}

object AlternativePaths {
  implicit val config: Configuration          = Configuration.default
  implicit val codec: Codec[AlternativePaths] = deriveUnwrappedCodec[AlternativePaths]
}

object AlternativePath {
  implicit val config: Configuration                  = Configuration.default
  implicit val codec: Codec.AsObject[AlternativePath] = deriveConfiguredCodec[AlternativePath]
}
