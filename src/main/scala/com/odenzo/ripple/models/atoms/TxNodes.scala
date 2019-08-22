package com.odenzo.ripple.models.atoms

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, HCursor}

import com.odenzo.ripple.models.atoms.RippleTxnType.{
  AccountSet,
  EscrowCancel,
  EscrowCreate,
  OfferCancel,
  OfferCreate,
  Payment,
  SetRegularKey,
  TrustSet
}

/**
  * These are the messages that exist in account_tx responses, inside transaction array
  *  and tx responses. Not used for things in the Meta OR ledger browsing.
  *  They are very similar to the Tx json reponse in submission though. Elaborate and unify, including common fields.
  *  REference Ripple source code for common fields.
  *  UNDER RECONSTRUCTION
  */
trait TxNode {

  def txType: RippleTxnType // This is the discriminator for subclass.
  def date: RippleTime
  def hash: TxnHash // This should be TxnHash as that is always what it is?
  def ledger_index: LedgerSequence
  def sequence: TxnSequence
}

/**
  *
  * @param sender
  * @param amount
  * @param dest
  * @param fee
  * @param flags
  * @param lastLedgerSequence    On submission on what ledger to expire this transaction.
  * @param memos
  * @param sequence
  * @param signingPubKey
  * @param txType
  * @param tx_sign
  * @param date
  * @param hash
  * @param ledger_index
  */
case class TxPayment(
    sender: AccountAddr,
    amount: CurrencyAmount,
    dest: AccountAddr,
    fee: Drops,
    flags: BitMask[PaymentFlag],                // TOD: Double check this!
    lastLedgerSequence: Option[LedgerSequence], // Depends on submission of request
    memos: Option[Memos],
    sequence: TxnSequence,
    signingPubKey: RipplePublicKey,
    txType: RippleTxnType,
    tx_sign: TxnSignature,
    date: RippleTime, // Only if validated when using TxRs
    hash: TxnHash,
    ledger_index: LedgerSequence
) extends TxNode

object TxPayment {

  implicit val decode: Decoder[TxPayment] = new Decoder[TxPayment] {
    final def apply(c: HCursor): Decoder.Result[TxPayment] = {
      for {
        sender             <- c.get[AccountAddr]("Account")
        amount             <- c.get[CurrencyAmount]("Amount")
        dest               <- c.get[AccountAddr]("Destination")
        fee                <- c.get[Drops]("Fee")
        flags              <- c.get[BitMask[PaymentFlag]]("Flags")
        lastLedgerSequence <- c.get[Option[LedgerSequence]]("LastLedgerSequence")
        memos              <- c.get[Option[Memos]]("Memos")
        sequence           <- c.get[TxnSequence]("Sequence")
        signingPubKey      <- c.get[RipplePublicKey]("SigningPubKey")
        txType             <- c.get[RippleTxnType]("TransactionType")
        tx_sign            <- c.get[TxnSignature]("TxnSignature")
        date               <- c.get[RippleTime]("date")
        hash               <- c.get[TxnHash]("hash")
        ledger_index       <- c.get[LedgerSequence]("ledger_index")
      } yield TxPayment(
        sender,
        amount,
        dest,
        fee,
        flags,
        lastLedgerSequence,
        memos,
        sequence,
        signingPubKey,
        txType,
        tx_sign,
        date,
        hash,
        ledger_index
      )

    }
  }
}

case class TxTrustSet(
    sender: AccountAddr,
    // amount: RippleAmount, // TrustSet never has
    // dest: AccountAddr,
    fee: Drops,
    flags: BitMask[TrustSetFlag],
    limitAmount: FiatAmount,                    // Always a FiatAmount
    lastLedgerSequence: Option[LedgerSequence], // Guess this is optional (always?)
    memos: Option[Memos],                       // Same from here and below
    sequence: TxnSequence,
    signingPubKey: RipplePublicKey,
    txType: RippleTxnType,
    tx_sig: String,
    date: RippleTime,
    hash: TxnHash,
    ledger_index: LedgerSequence
) extends TxNode

object TxTrustSet {

  implicit val decodeTxTrustSet: Decoder[TxTrustSet] = new Decoder[TxTrustSet] {
    final def apply(c: HCursor): Decoder.Result[TxTrustSet] = {

      for {
        sender             <- c.get[AccountAddr]("Account")
        fee                <- c.get[Drops]("Fee")
        flags              <- c.get[BitMask[TrustSetFlag]]("Flags")
        limitAmount        <- c.get[FiatAmount]("LimitAmount")
        lastLedgerSequence <- c.get[Option[LedgerSequence]]("LastLedgerSequence")
        memos              <- c.get[Option[Memos]]("Memos")
        sequence           <- c.get[TxnSequence]("Sequence")
        signingPubKey      <- c.get[RipplePublicKey]("SigningPubKey")
        txType             <- c.get[RippleTxnType]("TransactionType")
        tx_sign            <- c.get[String]("TxnSignature")
        date               <- c.get[RippleTime]("date")
        hash               <- c.get[TxnHash]("hash")
        ledger_index       <- c.get[LedgerSequence]("ledger_index")
      } yield TxTrustSet(
        sender,
        fee,
        flags,
        limitAmount,
        lastLedgerSequence,
        memos,
        sequence,
        signingPubKey,
        txType,
        tx_sign,
        date,
        hash,
        ledger_index
      )

    }
  }
}

case class TxAccountSet(
    account: AccountAddr,
    fee: Drops,
    flags: Long,                                // Clarify which flags return in tx and account_tx inquiries
    lastLedgerSequence: Option[LedgerSequence], // Guess this is optional (always?)
    memos: Option[Memos],                       // Same from here and below
    sequence: TxnSequence,
    signingPubKey: RipplePublicKey,
    txType: RippleTxnType,
    tx_sig: String,
    date: RippleTime,
    hash: TxnHash,
    ledger_index: LedgerSequence
) extends TxNode

object TxAccountSet {

  implicit val decodeTxAccountSet: Decoder[TxAccountSet] = new Decoder[TxAccountSet] {
    final def apply(c: HCursor): Decoder.Result[TxAccountSet] = {

      for {
        account            <- c.get[AccountAddr]("Account")
        fee                <- c.get[Drops]("Fee")
        flags              <- c.get[Long]("Flags")
        lastLedgerSequence <- c.get[Option[LedgerSequence]]("LastLedgerSequence")
        memos              <- c.get[Option[Memos]]("Memos")
        sequence           <- c.get[TxnSequence]("Sequence")
        signingPubKey      <- c.get[RipplePublicKey]("SigningPubKey")
        txType             <- c.get[RippleTxnType]("TransactionType")
        tx_sign            <- c.get[String]("TxnSignature") // This is a long hash?
        date               <- c.get[RippleTime]("date")
        hash               <- c.get[TxnHash]("hash")
        ledger_index       <- c.get[LedgerSequence]("ledger_index")
      } yield TxAccountSet(
        account,
        fee,
        flags,
        lastLedgerSequence,
        memos,
        sequence,
        signingPubKey,
        txType,
        tx_sign,
        date,
        hash,
        ledger_index
      )

    }
  }
}

case class TxSetRegularKey(
    account: AccountAddr,
    fee: Drops,
    flags: Long,
    regularKey: Option[String], // FIXME -- enumerate key types
    memos: Option[Memos],       // Same from here and below
    sequence: TxnSequence,
    signingPubKey: String,
    txType: RippleTxnType,
    tx_sig: String,
    date: RippleTime,
    hash: TxnHash,
    ledger_index: LedgerSequence
) extends TxNode

object TxSetRegularKey {

  implicit val decodeTxSetRegularKey: Decoder[TxSetRegularKey] = new Decoder[TxSetRegularKey] {
    final def apply(c: HCursor): Decoder.Result[TxSetRegularKey] = {
      for {
        account       <- c.get[AccountAddr]("Account")
        fee           <- c.get[Drops]("Fee")
        flags         <- c.get[Long]("Flags")
        regularKey    <- c.get[Option[String]]("RegularKey")
        memos         <- c.get[Option[Memos]]("Memos")
        sequence      <- c.get[TxnSequence]("Sequence")
        signingPubKey <- c.get[String]("SigningPubKey")
        txType        <- c.get[RippleTxnType]("TransactionType")
        tx_sign       <- c.get[String]("TxnSignature")
        date          <- c.get[RippleTime]("date")
        hash          <- c.get[TxnHash]("hash")
        ledger_index  <- c.get[LedgerSequence]("ledger_index")
      } yield TxSetRegularKey(
        account,
        fee,
        flags,
        regularKey,
        memos,
        sequence,
        signingPubKey,
        txType,
        tx_sign,
        date,
        hash,
        ledger_index
      )
    }
  }
}

/** Flags ommitted since only GLobal Flag */
case class TxEscrowCreate(
    account: AccountAddr,
    amount: Drops, // Only Drops can be escrowed
    destination: AccountAddr,
    cancelAfter: Option[RippleTime] = None,
    finishAfter: Option[RippleTime] = None, // as above, need a new type for this.
    condition: Option[String] = None,       // preimage-sha-256 crypto-condition.
    destinationTag: Option[AccountTag] = None,
    sourceTag: Option[AccountTag] = None,
    fee: Drops,
    lastLedgerSequence: Option[LedgerSequence],
    memos: Option[Memos],
    sequence: TxnSequence,
    signingPubKey: SigningPublicKey,
    transactionType: RippleTxnType,
    txnSignature: TxnSignature,
    date: RippleTime, // Never there on sign and submit result
    hash: TxnHash,
    ledger_index: LedgerSequence
) extends TxNode {

  override def txType: RippleTxnType = transactionType
}

object TxEscrowCreate {

  implicit val decodeTxSetRegularKey: Decoder[TxEscrowCreate] = new Decoder[TxEscrowCreate] {
    final def apply(c: HCursor): Decoder.Result[TxEscrowCreate] = {
      for {
        account       <- c.get[AccountAddr]("Account")
        amount        <- c.get[Drops]("Amount")
        destination   <- c.get[AccountAddr]("Destination")
        finishAfter   <- c.get[Option[RippleTime]]("FinishAfter")
        cancelAfter   <- c.get[Option[RippleTime]]("CancelAfter")
        condition     <- c.get[Option[String]]("Condition")
        destTag       <- c.get[Option[AccountTag]]("DestinationTag")
        srcTag        <- c.get[Option[AccountTag]]("SourceTag")
        fee           <- c.get[Drops]("Fee")
        memos         <- c.get[Option[Memos]]("Memos")
        sequence      <- c.get[TxnSequence]("Sequence")
        signingPubKey <- c.get[SigningPublicKey]("SigningPubKey")
        txType        <- c.get[RippleTxnType]("TransactionType")
        tx_sign       <- c.get[TxnSignature]("TxnSignature")
        date          <- c.get[RippleTime]("date")
        hash          <- c.get[TxnHash]("hash")
        ledger_index  <- c.get[LedgerSequence]("ledger_index")
      } yield TxEscrowCreate(
        account,
        amount,
        destination,
        cancelAfter,
        finishAfter,
        condition,
        destTag,
        srcTag,
        fee,
        None,
        memos,
        sequence,
        signingPubKey,
        txType,
        tx_sign,
        date,
        hash,
        ledger_index
      )
    }
  }
}

case class TxEscrowCancel(
    account: AccountAddr,
    owner: AccountAddr,
    offerSequence: Long, // FIXME: Need OfferSequence
    fee: Drops,
    flags: Long,          // Clarify which flags return in tx and account_tx inquiries
    memos: Option[Memos], // Same from here and below
    sequence: TxnSequence,
    signingPubKey: SigningPublicKey,
    txType: RippleTxnType,
    tx_sig: TxnSignature,
    date: RippleTime,
    hash: TxnHash,
    ledger_index: LedgerSequence
) extends TxNode

object TxEscrowCancel {

  implicit val decodeTxSetRegularKey: Decoder[TxEscrowCancel] = new Decoder[TxEscrowCancel] {
    final def apply(c: HCursor): Decoder.Result[TxEscrowCancel] = {
      for {
        account       <- c.get[AccountAddr]("Account")
        fee           <- c.get[Drops]("Fee")
        flags         <- c.get[Long]("Flags")
        memos         <- c.get[Option[Memos]]("Memos")
        offerSequence <- c.get[Long]("OfferSequence")
        owner         <- c.get[AccountAddr]("Owner")
        sequence      <- c.get[TxnSequence]("Sequence")
        signingPubKey <- c.get[SigningPublicKey]("SigningPubKey")
        txType        <- c.get[RippleTxnType]("TransactionType")
        tx_sign       <- c.get[TxnSignature]("TxnSignature")
        date          <- c.get[RippleTime]("date")
        hash          <- c.get[TxnHash]("hash")
        ledger_index  <- c.get[LedgerSequence]("ledger_index")
      } yield TxEscrowCancel(
        account,
        owner,
        offerSequence,
        fee,
        flags,
        memos,
        sequence,
        signingPubKey,
        txType,
        tx_sign,
        date,
        hash,
        ledger_index
      )
    }
  }
}

/** This does seem to differ a bit from the OfferCreateNode in ledger and as returned from BookOffers */
case class TxOfferCreate(
    account: AccountAddr,
    expiration: Option[RippleTime],
    offerSequence: Option[TxnSequence],
    takerGets: CurrencyAmount,
    takerPays: CurrencyAmount,
    fee: Drops,
    flags: Long,          // Clarify which flags return in tx and account_tx inquiries
    memos: Option[Memos], // Same from here and below
    sequence: TxnSequence,
    signingPubKey: RipplePublicKey,
    txType: RippleTxnType,
    tx_sig: String,
    date: RippleTime,
    hash: TxnHash,
    ledger_index: LedgerSequence
) extends TxNode

object TxOfferCreate {

  implicit val decodeTxSetRegularKey: Decoder[TxOfferCreate] = new Decoder[TxOfferCreate] {
    final def apply(c: HCursor): Decoder.Result[TxOfferCreate] = {
      for {
        account       <- c.get[AccountAddr]("Account")
        expiration    <- c.get[Option[RippleTime]]("Expirationxs")
        offerSequence <- c.get[Option[TxnSequence]]("OfferSequence")
        takerGets     <- c.get[CurrencyAmount]("TakerGets")
        takerPays     <- c.get[CurrencyAmount]("TakerPays")
        fee           <- c.get[Drops]("Fee")
        flags         <- c.get[Long]("Flags")
        memos         <- c.get[Option[Memos]]("Memos")

        //        owner         <- c.get[AccountAddr]("Owner")
        sequence      <- c.get[TxnSequence]("Sequence")
        signingPubKey <- c.get[RipplePublicKey]("SigningPubKey")
        txType        <- c.get[RippleTxnType]("TransactionType")
        tx_sign       <- c.get[String]("TxnSignature")
        date          <- c.get[RippleTime]("date")
        hash          <- c.get[TxnHash]("hash")
        ledger_index  <- c.get[LedgerSequence]("ledger_index")
      } yield TxOfferCreate(
        account,
        expiration,
        offerSequence,
        takerGets,
        takerPays,
        fee,
        flags,
        memos,
        sequence,
        signingPubKey,
        txType,
        tx_sign,
        date,
        hash,
        ledger_index
      )
    }
  }
}
case class TxOfferCancel(
    account: AccountAddr,
    fee: Drops,
    flags: Long,          // Clarify which flags return in tx and account_tx inquiries
    memos: Option[Memos], // Same from here and below
    offerSequence: OfferSequence,
    sequence: TxnSequence,
    signingPubKey: RipplePublicKey,
    txType: RippleTxnType,
    tx_sig: String,
    date: RippleTime,
    hash: TxnHash,
    ledger_index: LedgerSequence
) extends TxNode

object TxOfferCancel {

  implicit val decodeTxSetRegularKey: Decoder[TxOfferCancel] = new Decoder[TxOfferCancel] {
    final def apply(c: HCursor): Decoder.Result[TxOfferCancel] = {
      for {
        account       <- c.get[AccountAddr]("Account")
        fee           <- c.get[Drops]("Fee")
        flags         <- c.get[Long]("Flags")
        memos         <- c.get[Option[Memos]]("Memos")
        offerSequence <- c.get[OfferSequence]("OfferSequence")
        sequence      <- c.get[TxnSequence]("Sequence")
        signingPubKey <- c.get[RipplePublicKey]("SigningPubKey")
        txType        <- c.get[RippleTxnType]("TransactionType")
        tx_sign       <- c.get[String]("TxnSignature")
        date          <- c.get[RippleTime]("date")
        hash          <- c.get[TxnHash]("hash")
        ledger_index  <- c.get[LedgerSequence]("ledger_index")
      } yield TxOfferCancel(
        account,
        fee,
        flags,
        memos,
        offerSequence,
        sequence,
        signingPubKey,
        txType,
        tx_sign,
        date,
        hash,
        ledger_index
      )
    }
  }
}

object TxNode {
  implicit val rootDecoder: Decoder[TxNode] = new Decoder[TxNode] {
    override def apply(c: HCursor): Result[TxNode] = {
      c.get[RippleTxnType]("TransactionType").flatMap {
        case TrustSet      => c.as[TxTrustSet]
        case Payment       => c.as[TxPayment]
        case AccountSet    => c.as[TxAccountSet]
        case SetRegularKey => c.as[TxSetRegularKey]
        case EscrowCreate  => c.as[TxEscrowCreate]
        case EscrowCancel  => c.as[TxEscrowCancel]
        case OfferCreate   => c.as[TxOfferCreate]
        case OfferCancel   => c.as[TxOfferCancel]
        case other =>
          Left(DecodingFailure(s"Unknown TransactionType [$other]", c.history)) // FIXME: This is not surviving
      }
    }
  }

}
