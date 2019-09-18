package com.odenzo.ripple.models.wireprotocol.txns

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.Codec

import com.odenzo.ripple.models.atoms.{AccountAddr, SignerEntry, SignerEntries}
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.txns.TrustSetTx.{configCapitalize, wrapTxnCodec}

/** For setting or removing Multi-Signature List.
  *
  * [[https://ripple.com/build/transactions/#signerlistset]]
  * There are no specific flags for this transaction.
  * @param account
  * @param signerQuorum   Target number of Signer weights, 0 to delete a list.
  * @param signerEntries  Entries, omitted when deleting. 1-8 and weights greater than signerQuorum
  */
case class SignerListSetTx(
    account: AccountAddr,
    signerQuorum: Int,
    signerEntries: Option[SignerEntries]
) extends RippleTx

object SignerListSetTx extends CirceCodecUtils {
  implicit val config: Configuration                = configCapitalize.withDefaults
  private val base: Codec.AsObject[SignerListSetTx] = deriveConfiguredCodec[SignerListSetTx]

  implicit val codec: Codec.AsObject[SignerListSetTx] = wrapTxnCodec(base, RippleTxnType.SignerListSet)
}
