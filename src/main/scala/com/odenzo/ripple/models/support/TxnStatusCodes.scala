package com.odenzo.ripple.models.support

import scala.collection.immutable.NumericRange
import scala.collection.immutable.NumericRange.Inclusive

import enumeratum.{CirceEnum, EnumEntry, _}

import com.odenzo.ripple.models.utils.caterrors.CatsTransformers.ErrorOr
import com.odenzo.ripple.models.utils.caterrors.OError

/**
  * Ranges of errors have general meaning, with Transaction Command specific context
  */
sealed trait TxnStatusCode extends EnumEntry

/**
  * Technical problem here is that I want to associate the number code, but the actual JSON typically has the String
  * value (name). So, will have to do special goo if we want to parse the number based one.
  * OTHER PROBLEM IS HARD TO DEAL WITH ERRORS WHEN A NEW TransactionResult has an error.
  * Would like to add a .or on decoder or a default value.
  * [[https://github.com/ripple/rippled/blob/master/src/ripple/protocol/impl/TER.cpp]]
  * adn ]]https://github.com/ripple/rippled/blob/master/src/ripple/protocol/TER.h]]
  *
  */
case object TxnStatusCode extends Enum[TxnStatusCode] with CirceEnum[TxnStatusCode] {

  override val values = findValues

  // Note: Range is stable.  Exact numbers are currently unstable.  Use tokens.

  // -399 .. -300: L Local error (transaction fee inadequate, exceeds local limit)
  // Only valid during non-consensus processing.
  // Implications:
  // - Not forwarded
  // - No fee check
  case object telLOCAL_ERROR       extends TxnStatusCode // = -399
  case object telBAD_DOMAIN        extends TxnStatusCode
  case object telBAD_PATH_COUNT    extends TxnStatusCode
  case object telBAD_PUBLIC_KEY    extends TxnStatusCode
  case object telFAILED_PROCESSING extends TxnStatusCode
  case object telINSUF_FEE_P       extends TxnStatusCode
  case object telNO_DST_PARTIAL    extends TxnStatusCode
  case object telCAN_NOT_QUEUE     extends TxnStatusCode

  // -299 .. -200: M Malormed (bad signature)
  // Causes:
  // - Transaction corrupt.
  // Implications:
  // - Not applied
  // - Not forwarded
  // - Reject
  // - Can not succeed in any imagined ledger.
  case object temMALFORMED              extends TxnStatusCode //= -299
  case object temBAD_AMOUNT             extends TxnStatusCode
  case object temBAD_CURRENCY           extends TxnStatusCode
  case object temBAD_EXPIRATION         extends TxnStatusCode
  case object temBAD_FEE                extends TxnStatusCode
  case object temBAD_ISSUER             extends TxnStatusCode
  case object temBAD_LIMIT              extends TxnStatusCode
  case object temBAD_OFFER              extends TxnStatusCode
  case object temBAD_PATH               extends TxnStatusCode
  case object temBAD_PATH_LOOP          extends TxnStatusCode
  case object temBAD_SEND_XRP_LIMIT     extends TxnStatusCode
  case object temBAD_SEND_XRP_MAX       extends TxnStatusCode
  case object temBAD_SEND_XRP_NO_DIRECT extends TxnStatusCode
  case object temBAD_SEND_XRP_PARTIAL   extends TxnStatusCode
  case object temBAD_SEND_XRP_PATHS     extends TxnStatusCode
  case object temBAD_SEQUENCE           extends TxnStatusCode
  case object temBAD_SIGNATURE          extends TxnStatusCode
  case object temBAD_SRC_ACCOUNT        extends TxnStatusCode
  case object temBAD_TRANSFER_RATE      extends TxnStatusCode
  case object temDST_IS_SRC             extends TxnStatusCode
  case object temDST_NEEDED             extends TxnStatusCode
  case object temINVALID                extends TxnStatusCode
  case object temINVALID_FLAG           extends TxnStatusCode
  case object temREDUNDANT              extends TxnStatusCode
  case object temRIPPLE_EMPTY           extends TxnStatusCode
  case object temDISABLED               extends TxnStatusCode
  case object temBAD_SIGNER             extends TxnStatusCode
  case object temBAD_QUORUM             extends TxnStatusCode
  case object temBAD_WEIGHT             extends TxnStatusCode
  case object temBAD_TICK_SIZE          extends TxnStatusCode

  /** An intermediate result used internally, should never be returned. */
  case object temUNCERTAIN extends TxnStatusCode
  case object temUNKNOWN   extends TxnStatusCode

  // -199 .. -100: F
  //    Failure (sequence number previously used)
  //
  // Causes:
  // - Transaction cannot succeed because of ledger state.
  // - Unexpected ledger state.
  // - C++ exception.
  //
  // Implications:
  // - Not applied
  // - Not forwarded
  // - Could succeed in an imagined ledger.
  case object tefFAILURE          extends TxnStatusCode //= -199
  case object tefALREADY          extends TxnStatusCode
  case object tefBAD_ADD_AUTH     extends TxnStatusCode
  case object tefBAD_AUTH         extends TxnStatusCode
  case object tefBAD_LEDGER       extends TxnStatusCode
  case object tefCREATED          extends TxnStatusCode
  case object tefEXCEPTION        extends TxnStatusCode
  case object tefINTERNAL         extends TxnStatusCode
  case object tefNO_AUTH_REQUIRED extends TxnStatusCode // Can't set auth if auth is not required.

  case object tefPAST_SEQ          extends TxnStatusCode
  case object tefWRONG_PRIOR       extends TxnStatusCode
  case object tefMASTER_DISABLED   extends TxnStatusCode
  case object tefMAX_LEDGER        extends TxnStatusCode
  case object tefBAD_SIGNATURE     extends TxnStatusCode
  case object tefBAD_QUORUM        extends TxnStatusCode
  case object tefNOT_MULTI_SIGNING extends TxnStatusCode
  case object tefBAD_AUTH_MASTER   extends TxnStatusCode
  case object tefINVARIANT_FAILED  extends TxnStatusCode

  // -99 .. -1: R Retry
  //   sequence too high, no funds for txn fee, originating -account
  //   non-existent
  //
  // Cause:
  //   Prior application of another, possibly non-existent, transaction could
  //   allow this transaction to succeed.
  //
  // Implications:
  // - Not applied
  // - May be forwarded
  //   - Results indicating the txn was forwarded: terQUEUED
  //   - All others are not forwarded.
  // - Might succeed later
  // - Hold
  // - Makes hole in sequence which jams transactions.
  case object terRETRY       extends TxnStatusCode // = -99
  case object terFUNDS_SPENT extends TxnStatusCode
  case object terINSUF_FEE_B extends TxnStatusCode
  case object terNO_ACCOUNT  extends TxnStatusCode
  case object terNO_AUTH     extends TxnStatusCode
  case object terNO_LINE     extends TxnStatusCode
  case object terOWNERS      extends TxnStatusCode
  case object terPRE_SEQ     extends TxnStatusCode
  case object terLAST        extends TxnStatusCode
  case object terNO_RIPPLE   extends TxnStatusCode
  case object terQUEUED      extends TxnStatusCode

  // 0: S Success (success)
  // Causes:
  // - Success.
  // Implications:
  // - Applied
  // - Forwarded
  case object tesSUCCESS extends TxnStatusCode //= 0

  // 100 .. 159 C
  //   Claim fee only (ripple transaction with no good paths  case object pay to
  //   non-existent account  case object no path)
  //
  // Causes:
  // - Success  case object but does not achieve optimal result.
  // - Invalid transaction or no effect  case object but claim fee to use the sequence
  //   number.
  //
  // Implications:
  // - Applied
  // - Forwarded
  //
  // Only allowed as a return code of appliedTransaction when !tapRetry.
  // Otherwise  case object treated as terRETRY.
  //
  // DO NOT CHANGE THESE NUMBERS: They appear in ledger meta data.
  case object tecCLAIM                 extends TxnStatusCode //= 100
  case object tecPATH_PARTIAL          extends TxnStatusCode //= 101
  case object tecUNFUNDED_ADD          extends TxnStatusCode //= 102
  case object tecUNFUNDED_OFFER        extends TxnStatusCode //= 103
  case object tecUNFUNDED_PAYMENT      extends TxnStatusCode //= 104
  case object tecFAILED_PROCESSING     extends TxnStatusCode //= 105
  case object tecDIR_FULL              extends TxnStatusCode //= 121
  case object tecINSUF_RESERVE_LINE    extends TxnStatusCode //= 122
  case object tecINSUF_RESERVE_OFFER   extends TxnStatusCode //= 123
  case object tecNO_DST                extends TxnStatusCode //= 124
  case object tecNO_DST_INSUF_XRP      extends TxnStatusCode //= 125
  case object tecNO_LINE_INSUF_RESERVE extends TxnStatusCode //= 126
  case object tecNO_LINE_REDUNDANT     extends TxnStatusCode //= 127
  case object tecPATH_DRY              extends TxnStatusCode //= 128
  case object tecUNFUNDED              extends TxnStatusCode //= 129
  case object tecNO_ALTERNATIVE_KEY    extends TxnStatusCode //= 130
  case object tecNO_REGULAR_KEY        extends TxnStatusCode //= 131
  case object tecOWNERS                extends TxnStatusCode //= 132
  case object tecNO_ISSUER             extends TxnStatusCode //= 133
  case object tecNO_AUTH               extends TxnStatusCode //= 134
  case object tecNO_LINE               extends TxnStatusCode //= 135
  case object tecINSUFF_FEE            extends TxnStatusCode //= 136
  case object tecFROZEN                extends TxnStatusCode //= 137
  case object tecNO_TARGET             extends TxnStatusCode //= 138
  case object tecNO_PERMISSION         extends TxnStatusCode //= 139
  case object tecNO_ENTRY              extends TxnStatusCode //= 140
  case object tecINSUFFICIENT_RESERVE  extends TxnStatusCode //= 141
  case object tecNEED_MASTER_KEY       extends TxnStatusCode //= 142
  case object tecDST_TAG_NEEDED        extends TxnStatusCode //= 143
  case object tecINTERNAL              extends TxnStatusCode //= 144
  case object tecOVERSIZE              extends TxnStatusCode //= 145
  case object tecCRYPTOCONDITION_ERROR extends TxnStatusCode //= 146
  case object tecINVARIANT_FAILED      extends TxnStatusCode //= 147

//  // Partial list of detailed error codes, built on demand
//  case object tecCLAIM extends TxnStatusCode("tecCLAIM", 100L)
//
//  case object tecDIR_FULL extends TxnStatusCode("tecDIR_FULL", 121L)
//
//  case object tecINSUFFICIENT_RESERVE extends TxnStatusCode("tecINSUFFICIENT_RESERVE", 141L)
//
//  case object tecINSUF_RESERVE_LINE extends TxnStatusCode("tecINSUF_RESERVE_LINE", 122L)
//
//  case object tecINSUF_RESERVE_OFFER extends TxnStatusCode("tecINSUF_RESERVE_OFFER", 123L)
//
//  case object tecNO_AUTH extends TxnStatusCode("tecNO_AUTH", 134L)
//
//  case object tecNO_DST_INSUF_XRP extends TxnStatusCode("tecNO_DST_INSUF_XRP", 125L)
//
//  case object tecNO_ISSUER extends TxnStatusCode("tecNO_ISSUER", 133L)
//
//  case object tecNO_LINE_INSUF_RESERVE extends TxnStatusCode("tecNO_LINE_INSUF_RESERVE", 126L)
//
//  case object tecUNFUNDED_OFFER extends TxnStatusCode("tecUNFUNDED_OFFER", 103L)
//
//  case object tecUNFUNDED_PAYMENT extends TxnStatusCode("tecUNFUNDED_PAYMENT", 104L)
//
//  case object tesSUCCESS extends TxnStatusCode("tesSUCCESS", 0L)
//
//  case object tefPAST_SEQ extends TxnStatusCode("tefPAST_SEQ", -198L)
//
//  case object terQUEUED extends TxnStatusCode("terQUEUED", -89L)
//
//  case object tecPATH_PARTIAL extends TxnStatusCode("tecPATH_PARTIAL", -89L)
//
//  case object tecNO_TARGET extends TxnStatusCode("tecNO_TARGET", -89L)
}

/**
  * No real purpose to this yet, just wanted to see how good Scala ranges where.
  */
object TxnStatusCodeRanges {

  type CodeRange = NumericRange.Inclusive[Long]
  val constructRange: (Long, Long) => Inclusive[Long] = Range.Long.inclusive(_, _, 1)
  val txnSuccessRange                                 = TxnStatusCodeRange("tesSuccess", constructRange(0, 0))
  val txnTecRange                                     = TxnStatusCodeRange("tec", constructRange(100, 199))
  val txnTerRange                                     = TxnStatusCodeRange("ter", constructRange(-99, -1))
  val txnTefRange                                     = TxnStatusCodeRange("tef", constructRange(-199, -100))
  val txnTemRange                                     = TxnStatusCodeRange("tem", constructRange(-299, -200))
  val txnTelRange                                     = TxnStatusCodeRange("tel", constructRange(-399, -300))
  val allRanges                                       = Seq(txnSuccessRange, txnTecRange, txnTerRange, txnTefRange, txnTemRange, txnTelRange)

  def rangeByCode(code: Long): ErrorOr[TxnStatusCodeRange] = {
    import cats.implicits._
    val inRange: Option[TxnStatusCodeRange] = allRanges.find(r => r.range.containsTyped(code))
    Either.fromOption(inRange, OError(s"Code $code not found in any Txn Error Ranges"))
  }

  case class TxnStatusCodeRange(cat: String, range: NumericRange.Inclusive[Long])

}

/* TODO: Add multisign codes.

temBAD_SIGNER
temBAD_QUORUM
temBAD_WEIGHT
tefBAD_SIGNATURE
tefBAD_QUORUM
tefNOT_MULTI_SIGNING
tefBAD_AUTH_MASTER



 "engine_result" : "temBAD_EXPIRATION",
    "engine_result_code" : -296,
    "engine_result_message" : "Malformed: Bad expiration.",

 */
/*
tecCLAIM,                  { "tecCLAIM",                 "Fee claimed. Sequence used. No action."                                        } },
        { tecDIR_FULL,               { "tecDIR_FULL",              "Can not add entry to full directory."                                          } },
        { tecFAILED_PROCESSING,      { "tecFAILED_PROCESSING",     "Failed to correctly process transaction."                                      } },
        { tecINSUF_RESERVE_LINE,     { "tecINSUF_RESERVE_LINE",    "Insufficient reserve to add trust line."                                       } },
        { tecINSUF_RESERVE_OFFER,    { "tecINSUF_RESERVE_OFFER",   "Insufficient reserve to create offer."                                         } },
        { tecNO_DST,                 { "tecNO_DST",                "Destination does not exist. Send XRP to create it."                            } },
        { tecNO_DST_INSUF_XRP,       { "tecNO_DST_INSUF_XRP",      "Destination does not exist. Too little XRP sent to create it."                 } },
        { tecNO_LINE_INSUF_RESERVE,  { "tecNO_LINE_INSUF_RESERVE", "No such line. Too little reserve to create it."                                } },
        { tecNO_LINE_REDUNDANT,      { "tecNO_LINE_REDUNDANT",     "Can't set non-existent line to default."                                       } },
        { tecPATH_DRY,               { "tecPATH_DRY",              "Path could not send partial amount."                                           } },
        { tecPATH_PARTIAL,           { "tecPATH_PARTIAL",          "Path could not send full amount."                                              } },
        { tecNO_ALTERNATIVE_KEY,     { "tecNO_ALTERNATIVE_KEY",    "The operation would remove the ability to sign transactions with the account." } },
        { tecNO_REGULAR_KEY,         { "tecNO_REGULAR_KEY",        "Regular key is not set."                                                       } },
        { tecOVERSIZE,               { "tecOVERSIZE",              "Object exceeded serialization limits."                                         } },
        { tecUNFUNDED,               { "tecUNFUNDED",              "One of _ADD, _OFFER, or _SEND. Deprecated."                                    } },
        { tecUNFUNDED_ADD,           { "tecUNFUNDED_ADD",          "Insufficient XRP balance for WalletAdd."                                       } },
        { tecUNFUNDED_OFFER,         { "tecUNFUNDED_OFFER",        "Insufficient balance to fund created offer."                                   } },
        { tecUNFUNDED_PAYMENT,       { "tecUNFUNDED_PAYMENT",      "Insufficient XRP balance to send."                                             } },
        { tecOWNERS,                 { "tecOWNERS",                "Non-zero owner count."                                                         } },
        { tecNO_ISSUER,              { "tecNO_ISSUER",             "Issuer account does not exist."                                                } },
        { tecNO_AUTH,                { "tecNO_AUTH",               "Not authorized to hold asset."                                                 } },
        { tecNO_LINE,                { "tecNO_LINE",               "No such line."                                                                 } },
        { tecINSUFF_FEE,             { "tecINSUFF_FEE",            "Insufficient balance to pay fee."                                              } },
        { tecFROZEN,                 { "tecFROZEN",                "Asset is frozen."                                                              } },
        { tecNO_TARGET,              { "tecNO_TARGET",             "Target account does not exist."                                                } },
        { tecNO_PERMISSION,          { "tecNO_PERMISSION",         "No permission to perform requested operation."                                 } },
        { tecNO_ENTRY,               { "tecNO_ENTRY",              "No matching entry found."                                                      } },
        { tecINSUFFICIENT_RESERVE,   { "tecINSUFFICIENT_RESERVE",  "Insufficient reserve to complete requested operation."                         } },
        { tecNEED_MASTER_KEY,        { "tecNEED_MASTER_KEY",       "The operation requires the use of the Master Key."                             } },
        { tecDST_TAG_NEEDED,         { "tecDST_TAG_NEEDED",        "A destination tag is required."                                                } },
        { tecINTERNAL,               { "tecINTERNAL",              "An internal error has occurred during processing."                             } },
        { tecCRYPTOCONDITION_ERROR,  { "tecCRYPTOCONDITION_ERROR", "Malformed, invalid, or mismatched conditional or fulfillment."                 } },
        { tecINVARIANT_FAILED,       { "tecINVARIANT_FAILED",      "One or more invariants for the transaction were not satisfied."                } },

        { tefALREADY,                { "tefALREADY",               "The exact transaction was already in this ledger."                             } },
        { tefBAD_ADD_AUTH,           { "tefBAD_ADD_AUTH",          "Not authorized to add account."                                                } },
        { tefBAD_AUTH,               { "tefBAD_AUTH",              "Transaction's public key is not authorized."                                   } },
        { tefBAD_LEDGER,             { "tefBAD_LEDGER",            "Ledger in unexpected state."                                                   } },
        { tefBAD_QUORUM,             { "tefBAD_QUORUM",            "Signatures provided do not meet the quorum."                                   } },
        { tefBAD_SIGNATURE,          { "tefBAD_SIGNATURE",         "A signature is provided for a non-signer."                                     } },
        { tefCREATED,                { "tefCREATED",               "Can't add an already created account."                                         } },
        { tefEXCEPTION,              { "tefEXCEPTION",             "Unexpected program state."                                                     } },
        { tefFAILURE,                { "tefFAILURE",               "Failed to apply."                                                              } },
        { tefINTERNAL,               { "tefINTERNAL",              "Internal error."                                                               } },
        { tefMASTER_DISABLED,        { "tefMASTER_DISABLED",       "Master key is disabled."                                                       } },
        { tefMAX_LEDGER,             { "tefMAX_LEDGER",            "Ledger sequence too high."                                                     } },
        { tefNO_AUTH_REQUIRED,       { "tefNO_AUTH_REQUIRED",      "Auth is not required."                                                         } },
        { tefNOT_MULTI_SIGNING,      { "tefNOT_MULTI_SIGNING",     "Account has no appropriate list of multi-signers."                             } },
        { tefPAST_SEQ,               { "tefPAST_SEQ",              "This sequence number has already passed."                                      } },
        { tefWRONG_PRIOR,            { "tefWRONG_PRIOR",           "This previous transaction does not match."                                     } },
        { tefBAD_AUTH_MASTER,        { "tefBAD_AUTH_MASTER",       "Auth for unclaimed account needs correct master key."                          } },
        { tefINVARIANT_FAILED,       { "tefINVARIANT_FAILED",      "Fee claim violated invariants for the transaction."                            } },

        { telLOCAL_ERROR,            { "telLOCAL_ERROR",           "Local failure."                                                                } },
        { telBAD_DOMAIN,             { "telBAD_DOMAIN",            "Domain too long."                                                              } },
        { telBAD_PATH_COUNT,         { "telBAD_PATH_COUNT",        "Malformed: Too many paths."                                                    } },
        { telBAD_PUBLIC_KEY,         { "telBAD_PUBLIC_KEY",        "Public key too long."                                                          } },
        { telFAILED_PROCESSING,      { "telFAILED_PROCESSING",     "Failed to correctly process transaction."                                      } },
        { telINSUF_FEE_P,            { "telINSUF_FEE_P",           "Fee insufficient."                                                             } },
        { telNO_DST_PARTIAL,         { "telNO_DST_PARTIAL",        "Partial payment to create account not allowed."                                } },
        { telCAN_NOT_QUEUE,          { "telCAN_NOT_QUEUE",         "Can not queue at this time." } },

        { temMALFORMED,              { "temMALFORMED",             "Malformed transaction."                                                        } },
        { temBAD_AMOUNT,             { "temBAD_AMOUNT",            "Can only send positive amounts."                                               } },
        { temBAD_CURRENCY,           { "temBAD_CURRENCY",          "Malformed: Bad currency."                                                      } },
        { temBAD_EXPIRATION,         { "temBAD_EXPIRATION",        "Malformed: Bad expiration."                                                    } },
        { temBAD_FEE,                { "temBAD_FEE",               "Invalid fee, negative or not XRP."                                             } },
        { temBAD_ISSUER,             { "temBAD_ISSUER",            "Malformed: Bad issuer."                                                        } },
        { temBAD_LIMIT,              { "temBAD_LIMIT",             "Limits must be non-negative."                                                  } },
        { temBAD_OFFER,              { "temBAD_OFFER",             "Malformed: Bad offer."                                                         } },
        { temBAD_PATH,               { "temBAD_PATH",              "Malformed: Bad path."                                                          } },
        { temBAD_PATH_LOOP,          { "temBAD_PATH_LOOP",         "Malformed: Loop in path."                                                      } },
        { temBAD_QUORUM,             { "temBAD_QUORUM",            "Malformed: Quorum is unreachable."                                             } },
        { temBAD_SEND_XRP_LIMIT,     { "temBAD_SEND_XRP_LIMIT",    "Malformed: Limit quality is not allowed for XRP to XRP."                       } },
        { temBAD_SEND_XRP_MAX,       { "temBAD_SEND_XRP_MAX",      "Malformed: Send max is not allowed for XRP to XRP."                            } },
        { temBAD_SEND_XRP_NO_DIRECT, { "temBAD_SEND_XRP_NO_DIRECT","Malformed: No Ripple direct is not allowed for XRP to XRP."                    } },
        { temBAD_SEND_XRP_PARTIAL,   { "temBAD_SEND_XRP_PARTIAL",  "Malformed: Partial payment is not allowed for XRP to XRP."                     } },
        { temBAD_SEND_XRP_PATHS,     { "temBAD_SEND_XRP_PATHS",    "Malformed: Paths are not allowed for XRP to XRP."                              } },
        { temBAD_SEQUENCE,           { "temBAD_SEQUENCE",          "Malformed: Sequence is not in the past."                                       } },
        { temBAD_SIGNATURE,          { "temBAD_SIGNATURE",         "Malformed: Bad signature."                                                     } },
        { temBAD_SIGNER,             { "temBAD_SIGNER",            "Malformed: No signer may duplicate account or other signers."                  } },
        { temBAD_SRC_ACCOUNT,        { "temBAD_SRC_ACCOUNT",       "Malformed: Bad source account."                                                } },
        { temBAD_TRANSFER_RATE,      { "temBAD_TRANSFER_RATE",     "Malformed: Transfer rate must be >= 1.0"                                       } },
        { temBAD_WEIGHT,             { "temBAD_WEIGHT",            "Malformed: Weight must be a positive value."                                   } },
        { temDST_IS_SRC,             { "temDST_IS_SRC",            "Destination may not be source."                                                } },
        { temDST_NEEDED,             { "temDST_NEEDED",            "Destination not specified."                                                    } },
        { temINVALID,                { "temINVALID",               "The transaction is ill-formed."                                                } },
        { temINVALID_FLAG,           { "temINVALID_FLAG",          "The transaction has an invalid flag."                                          } },
        { temREDUNDANT,              { "temREDUNDANT",             "Sends same currency to self."                                                  } },
        { temRIPPLE_EMPTY,           { "temRIPPLE_EMPTY",          "PathSet with no paths."                                                        } },
        { temUNCERTAIN,              { "temUNCERTAIN",             "In process of determining result. Never returned."                             } },
        { temUNKNOWN,                { "temUNKNOWN",               "The transaction requires logic that is not implemented yet."                   } },
        { temDISABLED,               { "temDISABLED",              "The transaction requires logic that is currently disabled."                    } },
        { temBAD_TICK_SIZE,          { "temBAD_TICK_SIZE",         "Malformed: Tick size out of range."                                            } },

        { terRETRY,                  { "terRETRY",                 "Retry transaction."                                                            } },
        { terFUNDS_SPENT,            { "terFUNDS_SPENT",           "Can't set password, password set funds already spent."                         } },
        { terINSUF_FEE_B,            { "terINSUF_FEE_B",           "Account balance can't pay fee."                                                } },
        { terLAST,                   { "terLAST",                  "Process last."                                                                 } },
        { terNO_RIPPLE,              { "terNO_RIPPLE",             "Path does not permit rippling."                                                } },
        { terNO_ACCOUNT,             { "terNO_ACCOUNT",            "The source account does not exist."                                            } },
        { terNO_AUTH,                { "terNO_AUTH",               "Not authorized to hold IOUs."                                                  } },
        { terNO_LINE,                { "terNO_LINE",               "No such line."                                                                 } },
        { terPRE_SEQ,                { "terPRE_SEQ",               "Missing/inapplicable prior transaction."                                       } },
        { terOWNERS,                 { "terOWNERS",                "Non-zero owner count."                                                         } },
        { terQUEUED,                 { "terQUEUED",                "Held until escalated fee drops."                                               } },

        { tesSUCCESS,                { "tesSUCCESS",               "The transaction was applied. Only final in a validated ledger."

 */
