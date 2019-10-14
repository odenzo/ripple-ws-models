package com.odenzo.ripple.models.support

import com.odenzo.ripple.models.atoms._

/**
  * Bootstrapping a Ripple system always creates this Genesis Account
  * sh-4.2# ./rippled -q wallet_propose masterpassphrase
  * {
  * "id" : 1,
  * "result" : {
  * "account_id" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh",
  * "key_type" : "secp256k1",
  * "master_key" : "I IRE BOND BOW TRIO LAID SEAT GOAL HEN IBIS IBIS DARE",
  * "master_seed" : "snoPBrXtMeMyMHUVTgbuqAfg1SUTb",
  * "master_seed_hex" : "DEDCE9CE67B451D852FD4E846FCDE31C",
  * "public_key" : "aBQG8RQAzjs1eTKFEAQXr2gS4utcDiEC9wmi7pfUPTi27VCahwgw",
  * "public_key_hex" : "0330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD020",
  * "status" : "success",
  * "warning" : "This wallet was generated using a user-supplied passphrase that has low entropy and is vulnerable to brute-force attacks."
  * }
  * }
  */
object GenesisAccount {

  val passphrase: RipplePassphrase = RipplePassphrase("masterpassphrase")
  val address: AccountAddr         = AccountAddr("rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh")
  val masterSeed: RippleSeed       = RippleSeed(Base58Checksum("snoPBrXtMeMyMHUVTgbuqAfg1SUTb"))
  val secret: RippleSeed           = masterSeed

  val accountKeys: AccountKeys = AccountKeys(
    address,
    RippleKeyType("secp256k1"),
    RippleKey(RFC1751("I IRE BOND BOW TRIO LAID SEAT GOAL HEN IBIS IBIS DARE")),
    masterSeed,
    RippleSeedHex("DEDCE9CE67B451D852FD4E846FCDE31C"),
    RipplePublicKey(Base58Checksum("aBQG8RQAzjs1eTKFEAQXr2gS4utcDiEC9wmi7pfUPTi27VCahwgw")),
    RipplePublicKeyHex("0330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD020")
  )

  val fkp: FullKeyPair          = FullKeyPair(accountKeys, None)
  override def toString: String = "GENESIS"
}

/**
  * The XRP Issuer Account
  */
object AccountZero {
  val address                   = AccountAddr("rrrrrrrrrrrrrrrrrrrrrhoLvTp")
  override def toString: String = "ACCOUNT_ZERO"

}

/**
  * Neutral Account used in Ledger Info. A go between. Will show up in RippleState type of LedgerEntry
  */
object AccountOne {
  val address                   = AccountAddr("rrrrrrrrrrrrrrrrrrrrBZbvji")
  override def toString: String = "ACCOUNT_ONE"

}
