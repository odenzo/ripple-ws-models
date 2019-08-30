
# Transaction Overview

## Structure

This package has the core transaction information that gets corresponds to tx_json.

These are used for
 - *constructing* requests to be signed and submitted
 - parsing the responses from sign/signFor/submit responses.
 - In the decoding of various inquiry commands and ledger items.
 
 Each of these have slightly different contexts.
 For creating messages, TxOptions are used to add generic information, including some information that was "auto-fill", 
 e.g. finding out appropriate values for Fee, MaxLedgerIndex, LastSequence, AccountSequence

* Payment channels not tests
* Escrow not tested
* DepositPreauht not implemented yet.
* Check [Create|Cancel|Cash] no implmented yet



* All transactions are typically merged with TxOption to generate complete message.
* Also the CanonicalSig flag is automatically set no matter what the other flag bits are
