package com.odenzo.ripple.models.atoms.ledgertree.statenodes

import cats.Show

import com.odenzo.ripple.models.atoms.ledgertree.LedgerNodeIndex
import com.odenzo.ripple.models.atoms.{AccountAddr, UInt64, RippleHash}

// This has some standard additional fields too
// https://xrpl.org/check.html
case class CheckNode(
    account: AccountAddr,
    destination: AccountAddr
) extends LedgerNode
