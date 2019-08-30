package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleScrollingRq, RippleScrollingRs}
import com.odenzo.ripple.models.utils.CirceCodecUtils

/** TODO: This is not used yet, so not fully implemented
  * See  https://ripple.com/build/rippled-apis/#account-objects
  * Note that this has scrolling results
  * This seems to have some gaps in the documentation.
  */
case class AccountObjectsRq(
    account: AccountAddr,
    tipe: Option[String],
    ledger: LedgerID = LedgerName.VALIDATED_LEDGER,
    limit: Limit = Limit.default,
    marker: Option[Marker] = None,
    id: RippleMsgId = RippleMsgId.random
) extends RippleScrollingRq

case class AccountObjectsRs(
    account: AccountAddr,
    account_objects: List[Json],
    marker: Option[Marker],
    resultLedger: Option[ResultLedger]
) extends RippleScrollingRs

object AccountObjectsRq extends CirceCodecUtils {
  implicit val encoder: Encoder.AsObject[AccountObjectsRq] = {
    deriveEncoder[AccountObjectsRq]
      .mapJsonObject(withCommandAndLedgerID("account_objects") andThen withRenameField("tipe", "type"))

  }
}

object AccountObjectsRs {

  implicit val decoder: Decoder[AccountObjectsRs] = {
    deriveDecoder[AccountObjectsRs]
      .product(Decoder[ResultLedger])
      .map {
        case (a, theResultLedger) => a.copy(resultLedger = Some(theResultLedger))
      }
  }
}
