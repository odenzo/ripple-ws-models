package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.AccountData
import com.odenzo.ripple.models.support.{RippleRs, RippleRq}

/**
  * https://ripple.com/build/rippled-apis/#account-info
  *
  * @param account  Set to strict so only public key or Account Address, we only allow addr.
  * @param queue
  * @param signer_lists
  * @param strict
  * @param ledger  Ledger Hash (20byte hex) or LedgerIndex including keywords
  * @param id
  */
case class AccountInfoRq(
    account: Account,
    queue: Boolean = false,
    signer_lists: Boolean = true,
    strict: Boolean = true,
    ledger: Ledger = LedgerName.CURRENT_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq {}

case class AccountInfoRs(
    account_data: AccountData,
    signer_lists: Option[List[Signer]], // Really SignerList ledger object SignerListNode
    queue_data: Option[Json],           // Docs say this is outside. Signers inside.
    validated: Option[Boolean],         // Field may not be there, means false is not there.
    resultLedger: ResultLedger          // ledger_index or ledger_current_index field
) extends RippleRs

object AccountInfoRq {
  val command: (String, Json) = "command" -> Json.fromString("account_info")
  implicit val encoder: Encoder.AsObject[AccountInfoRq] = {
    deriveEncoder[AccountInfoRq]
      .mapJsonObject(o => command +: o)
      .mapJsonObject(o => Ledger.renameLedgerField(o))
  }

}

object AccountInfoRs {

  import io.circe._
  import io.circe.generic.extras.semiauto._
  implicit val config: Configuration                    = Configuration.default
  implicit val encoder: Encoder.AsObject[AccountInfoRs] = deriveConfiguredEncoder[AccountInfoRs]

  /** This is somewhat manual, but responses have several standard fields and then only 1-3 specific subobjects
    * normally, so maybe building a utlity lib from this?, e.g. queue_data, mark for scrolling etc. */
  implicit val decoder: Decoder[AccountInfoRs] = Decoder.instance { hc =>
    for {
      resultLedger <- hc.as[ResultLedger]
      signers      <- hc.get[Option[List[Signer]]]("signer_lists")
      queuedata    <- hc.get[Option[Json]]("queue_data")
      accountdata  <- hc.get[AccountData]("account_data")
      validated    <- hc.get[Option[Boolean]]("validated")
    } yield AccountInfoRs(accountdata, signers, queuedata, validated, resultLedger)
  }
//    Encoder.encodeEither("leftName", "rightName")
//  val d2 = Decoder.decodeEither("leftname", "rightname")
}
