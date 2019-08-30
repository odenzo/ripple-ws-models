package com.odenzo.ripple.models.wireprotocol.accountinfo

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.AccountData
import com.odenzo.ripple.models.support.{RippleRs, RippleRq}
import com.odenzo.ripple.models.utils.CirceCodecUtils

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
    ledger: LedgerID = LedgerName.CURRENT_LEDGER,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq {}

case class AccountInfoRs(
    account_data: AccountData,
    signer_lists: Option[List[Signer]], // Really SignerList ledger object SignerListNode
    queue_data: Option[Json],           // Docs say this is outside. Signers inside.
    validated: Boolean = false,         // Field may not be there, means false is not there.
    resultLedger: ResultLedger          // ledger_index or ledger_current_index field
) extends RippleRs

object AccountInfoRq extends CirceCodecUtils {

  implicit val encoder: Encoder.AsObject[AccountInfoRq] = {
    deriveEncoder[AccountInfoRq].mapJsonObject(withCommandAndLedgerID("account_info"))
  }

}

object AccountInfoRs {

  import io.circe._
  import io.circe.generic.extras.semiauto._
  implicit val config: Configuration                = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[AccountInfoRs] = deriveConfiguredCodec[AccountInfoRs]
}
