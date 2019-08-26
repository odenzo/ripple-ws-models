package com.odenzo.ripple.models.atoms

import cats.Show
import io.circe._
import io.circe.syntax._

import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._

sealed trait Account

object Account {

  /**
    * Need to review how a dt is encoded in one string
    *
    * @param account Either a ~name or native account number. Currently no validation
    *
    * @return
    */
  def from(account: String): Account = {
    val a = account.trim
    if (a.startsWith("~")) AccountAlias(a)
    else AccountAlias(a)
  }

  implicit val encoder: Encoder[Account] = Encoder.instance {
    case addr: AccountAddr   => addr.asJson
    case alias: AccountAlias => alias.asJson
  }

}

/** Represents a raw Ripple Account Address (sometimes called accountId (e.g. AccountKeys payload) */
final case class AccountAddr(address: String) extends Account {
  require(address.startsWith("r"), s"Ripple Account Address doesnt start with r :: [$address]")

}
object AccountAddr {

  implicit val codec: Codec[AccountAddr] = deriveUnwrappedCodec[AccountAddr]
  implicit val show: Show[AccountAddr]   = Show.show[AccountAddr](v => v.address)

}

/** This is like ~odenzo, also known as NickName. Cannot send in request like AccountLine anymore.
  * Google says these aren't actually stored in the ledger but in an external database
  * but bitchomp has them. Leave as read only type thing and not pass in requests.
  *
  **/
final case class AccountAlias(account: String) extends Account {
  require(account.startsWith("~"), "Ripple Aliases must start with ~ character")

}

object AccountAlias {
  implicit val codec: Codec[AccountAlias] = deriveUnwrappedCodec[AccountAlias]
}

/**
  * Use for dt:string addressing of an account mapped to a single ripple address (e.g. a gateway account)
  * @param tag
  */
case class AccountTag(tag: UInt32)

object AccountTag {
  def apply(dt: Long): AccountTag         = AccountTag(UInt32(dt))
  implicit val decoder: Codec[AccountTag] = deriveUnwrappedCodec[AccountTag]
}

/** Note this is not valid for most of the WebSocket Requests
  * And I guess technically it is (acc: Account, dt:String)
  * since can use aliases
  * JSON Formats quite different normally.
  * FIXME: Broken migrate to AccountTag product encoding/decoding
  * */
case class DTAccountAddr(address: AccountAddr, dt: AccountTag) {
  def toValue = s"$address:$dt" // FIXME: Confirm Use-Case
}

object DTAccountAddr {
  implicit val encoder: Encoder[DTAccountAddr] = Encoder[String].contramap[DTAccountAddr](v => s"${v.address}:${v.dt}")

}
