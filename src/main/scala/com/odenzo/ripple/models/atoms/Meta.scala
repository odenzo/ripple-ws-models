package com.odenzo.ripple.models.atoms

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms.ledgertree.AffectedLedgerNode
import com.odenzo.ripple.models.support.TxnStatusCode
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * The meta element is contained in results of following Ripple calls: X,Y,Z
  * It has been tested with : account_tx only.
  * The basic idea here is that entries have "delta"s of ledger nodes.
  *
  * @param affectedNodes List of created, deleted or modified ledger nodes
  * @param transactionIndex The index of the transaction within the ledger it was committed.
  * @param transactionResult The Transaction Engine Result, e.g. tesSuccess. On succes its tesSUCCESS, not sure error cases.
  * @param delivered_amount The actual amount delivered for money movements only. (TODO: CONFIRM THIS EXISTS)
  */
case class Meta(
    affectedNodes: List[AffectedLedgerNode],
    transactionIndex: TxnIndex, // Is this index for account or for ledger?
    transactionResult: TxnStatusCode,
    delivered_amount: Option[CurrencyAmount]
)

object Meta extends CirceCodecUtils {
  implicit val config: Configuration       = CirceCodecUtils.capitalizeExcept(Set("delivered_amount"))
  implicit val codec: Codec.AsObject[Meta] = deriveConfiguredCodec[Meta]
}
