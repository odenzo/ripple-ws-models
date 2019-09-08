package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxCommon
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods.TxHistoryRq.wrapCommandCodec
import com.odenzo.ripple.models.wireprotocol.txns.RippleTx

case class TxRq(transaction: TxnHash, binary: Boolean = false) extends RippleRq

/**
  *
  */
case class TxRs(tx: RippleTx, common: TxCommon) extends RippleRs

object TxRq {
  private type ME = TxRq
  private val command: String            = "tx"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec)
}

object TxRs extends CirceCodecUtils {

  import io.circe._

  implicit val decoder: Decoder[TxRs] = Decoder[RippleTx].product(Decoder[TxCommon]).map(v => TxRs(v._1, v._2))
  implicit val encoder: Encoder.AsObject[TxRs] =
    Encoder.AsObject.instance[TxRs](a => combineEncodedJsonObjects(a.tx, a.common))

}
