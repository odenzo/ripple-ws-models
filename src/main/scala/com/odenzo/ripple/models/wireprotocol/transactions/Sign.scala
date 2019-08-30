package com.odenzo.ripple.models.wireprotocol.transactions

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Json, _}

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.atoms.ledgertree.transactions.TxCommon
import com.odenzo.ripple.models.support.{RippleRs, RippleRq}
import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.support.RippleTransaction

/**
  * [[https://ripple.com/build/rippled-apis/#sign]]
  *
  * @param tx_json   The actual transaction data
  * @param seed_hex  Changes so must use master_seed_hex
  * @param fee_multi_max
  * @param build_path Looks for presence now, so None to false and (default) Some(true) to build_path automatically
  * @param id
  */
case class SignRq(
    tx_json: Json,
    seed_hex: RippleSeedHex,
    offline: Boolean = false,
    build_path: Option[Boolean] = None,
    fee_multi_max: Option[Long] = Some(1000L),
    fee_div_max: Option[Long] = None,
    key_type: String,
    id: RippleMsgId = RippleMsgId.random
) extends RippleRq

/**
  * tx_json is SignRq + SignData
  *
  */
case class SignRs(tx_blob: TxBlob, signed: TxCommon, tx: RippleTransaction) extends RippleRs

object SignRq {

  val command: (String, Json) = "command" -> Json.fromString("sign")
  implicit val encoder: Encoder.AsObject[SignRq] = {
    deriveEncoder[SignRq]
      .mapJsonObject(o => command +: o)
  }
}

object SignRs {
  //implicit val decoder: Decoder[SignRs] = deriveDecoder[SignRs]

  implicit val decoder2: Decoder[SignRs] = Decoder.instance { c =>
    for {
      txblob <- c.get[TxBlob]("tx_blob")
      signed <- c.get[TxCommon]("tx_json")
      txrs   <- c.get[RippleTransaction]("tx_json")
    } yield SignRs(txblob, signed, txrs)
  }
}
