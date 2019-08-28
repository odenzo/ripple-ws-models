package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import cats.Show

import com.odenzo.ripple.models.atoms.AccountAddr

// This has some standard additional fields too
// https://xrpl.org/check.html
case class DepositPreAuthNode(
    account: AccountAddr,
    authorize: AccountAddr
    // ...
) extends LedgerNode
