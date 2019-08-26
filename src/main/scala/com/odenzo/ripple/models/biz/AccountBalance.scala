package com.odenzo.ripple.models.biz

import com.odenzo.ripple.models.atoms.{AccountAddr, CurrencyAmount}

case class AccountBalance(addr: AccountAddr, balance: CurrencyAmount)
