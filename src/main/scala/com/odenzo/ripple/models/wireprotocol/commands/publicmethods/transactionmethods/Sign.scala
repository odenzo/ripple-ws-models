package com.odenzo.ripple.models.wireprotocol.commands.publicmethods.transactionmethods

import io.circe.generic.extras.Configuration
import io.circe._
import io.circe.generic.extras.semiauto._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils
import com.odenzo.ripple.models.wireprotocol.commands.{RippleRs, RippleRq}

/**
  * [[https://ripple.com/build/rippled-apis/#sign]]
  *
  * @param tx_json   The actual transaction data
  * @param seed_hex  Changes so must use master_seed_hex
  * @param fee_multi_max
  * @param build_path Looks for presence now, so None to false and (default) Some(true) to build_path automatically
  */
case class SignRq(
    tx_json: JsonObject,
    seed_hex: RippleSeedHex,
    key_type: String,
    offline: Boolean            = false,
    build_path: Option[Boolean] = None,
    fee_multi_max: Option[Long] = Some(1000L),
    fee_div_max: Option[Long]   = None
) extends RippleRq

/**
  * Note any Common Fields shoud use TxCommon to extract, see also (in flux) LedgerTxn
  *
  */
case class SignRs(tx_blob: TxBlob, tx_json: JsonObject) extends RippleRs

object SignRq extends CirceCodecUtils {
  private type ME = SignRq
  private val command: String            = "sign"
  implicit val config: Configuration     = Configuration.default.withDefaults
  implicit val codec: Codec.AsObject[ME] = wrapCommandCodec(command, deriveConfiguredCodec[ME])

}

object SignRs extends CirceCodecUtils {

  implicit val config: Configuration         = Configuration.default
  implicit val codec: Codec.AsObject[SignRs] = deriveConfiguredCodec[SignRs]
}
