package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.utils.CirceCodecUtils

/**
  * See also docs for account root node. I am guessing this has delta too?
  *
  */
case class SignerListNode(
    flags: Long,
    ownerNode: Option[String],
    previousTxnId: Option[TxnHash],
    previousTxnLgrSeq: Option[LedgerSequence],
    signerEntries: List[SignerEntry],
    signerListID: Long,
    signerQuorum: Long,
    index: LedgerHash
) extends LedgerNode

object SignerListNode extends CirceCodecUtils {

  implicit val config: Configuration = configCapitalizeExcept(Set("index"))

  private implicit val cowrapper: Codec[List[SignerEntry]] = wrapListOfNestedObj[SignerEntry]("SignerEntry")
  implicit val codec: Codec.AsObject[SignerListNode]       = deriveConfiguredCodec[SignerListNode]

}
