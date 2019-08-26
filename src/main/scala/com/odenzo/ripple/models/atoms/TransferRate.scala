package com.odenzo.ripple.models.atoms

import io.circe.Codec
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec

import com.odenzo.ripple.models.utils.caterrors.OError

/**
  * The TransferRate field specifies a fee to charge whenever counterparties transfer the currency you issue.
  * See Transfer Fees article in the Knowledge Center for more information.
  **
  *In rippled's WebSocket and JSON-RPC APIs, the TransferRate is represented as an integer,
  * the amount that must be sent for 1 billion units to arrive.
  * For example, a 20% transfer fee is represented as the value 1200000000. The value cannot be less than 1000000000.
  * (Less than that would indicate giving away money for sending transactions, which is exploitable.)
  * You can specify 0 as a shortcut for 1000000000, meaning no fee.
  *
  * @param rate
  */
case class TransferRate(rate: Long)

object TransferRate {

  implicit val codec: Codec[TransferRate] = deriveUnwrappedCodec[TransferRate]

  val billion      = 1000000000L
  private val base = billion / 100L

  val noFee = TransferRate(billion)

  /**
    * Make a transfer rate based on percentage which yields amount (as percent of transaction) to charge in fees.
    *
    * @param percentage value between 0 and 100 representing 0% and 100%
    * @return
    */
  def fromPercentage(percentage: Double): Either[OError, TransferRate] = {

    Either.cond(
      percentage >= 0.0 && percentage <= 100.0,
      calc(percentage),
      OError(s"Transfer Rate % $percentage not between 0 and 100")
    )
  }

  /**
    * Calculates percentage by on 20% == 20 style and truncates down to Long value
    * @param p
    */
  private def calc(p: Double): TransferRate = {
    val z: Long = (base * p).toLong + billion
    TransferRate(z)
  }

}
