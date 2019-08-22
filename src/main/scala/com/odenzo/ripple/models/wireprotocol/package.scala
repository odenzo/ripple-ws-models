package com.odenzo.ripple.models

/**
  * This contains the inquiry ripple requests and response model objects, at top level, sometimes result field level,
  * that correspond to   https://ripple.com/build/rippled-apis/
  * The subcategorization (Account Information, Ledger Information, Transactions, Subscriptions, Server Information
  * are in the corresponding sub-packages for each of finding.
  * <p>
  * The Transacstions are a bit of an exception. The documentation on website combines transaction and then
  * the actual Transactions types at  https://ripple.com/build/transactions/  The top level transaction
  * types are modeled under wireprotocal.transaction.transactiontypes
  * </p>
  * <p>
  *   The models.atoms also model the elements in the rippled-api that are used across the different requests/responses.
  *   The goal is to have a type-safe system as much as possible. Still some gaps and confusion regarding hashes etc.
  *   Sometimes the re-usable atoms have the same "hash" or something, but with different semantic meaning depending
  *   on the context. Or maybe I am just confused.
  *   Will iteratively flesh these out.
  *   </p>
  *
  */
package object wireprotocol {}
