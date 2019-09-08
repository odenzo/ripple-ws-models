package com.odenzo.ripple.models.support

import io.circe.Codec
import io.circe.generic.semiauto._

import com.odenzo.ripple.models.atoms.{AccountAddr, SigningPublicKey, AccountKeys, RippleSeed}

/* Has master key and optional regular key */
case class FullKeyPair(master: AccountKeys, regular: Option[AccountKeys]) {

  /** Regular AccountKeys if defined else Master AccountKeys */
  def key: AccountKeys = regular.getOrElse(master)

  /** Always returns the account address from the master seed */
  def address: AccountAddr = master.address

  /** Returns the regular key seed if exists else master key seed */
  def seed: RippleSeed = key.master_seed

  def singingPubKey: SigningPublicKey = key.signingPubKey

}

object FullKeyPair {

  val genesis: FullKeyPair = FullKeyPair(GenesisAccount.accountKeys, None)

  implicit val codec: Codec.AsObject[FullKeyPair] = deriveCodec[FullKeyPair]

  def apply(master: AccountKeys): FullKeyPair                   = FullKeyPair(master, None)
  def apply(master: AccountKeys, reg: AccountKeys): FullKeyPair = FullKeyPair(master, Some(reg))
}
