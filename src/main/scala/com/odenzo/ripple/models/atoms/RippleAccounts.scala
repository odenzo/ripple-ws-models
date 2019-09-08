package com.odenzo.ripple.models.atoms

import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.semiauto._
import cats.{Show, _}
import cats.data._
import cats.implicits._

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
    else AccountAddr(a)
  }

  implicit val encoder: Encoder[Account] = Encoder.instance {
    case addr: AccountAddr   => addr.asJson
    case alias: AccountAlias => alias.asJson
  }

  implicit val decoder: Decoder[Account] = List[Decoder[Account]](
    Decoder[AccountAlias].widen,
    Decoder[AccountAddr].widen
  ).reduceLeft(_ or _)
}

/** Represents a raw Ripple Account Address (sometimes called accountId (e.g. AccountKeys payload)
  * TODO: Base58Check */
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
  //require(account.startsWith("~"), "Ripple Aliases must start with ~ character")

}

object AccountAlias {
  val codec: Codec[AccountAlias] = deriveUnwrappedCodec[AccountAlias]
  implicit val decoder: Decoder[AccountAlias] =
    codec.ensure(alias => alias.account.startsWith("~"), "Account Aliases must start with ~")

  implicit val encoder: Encoder[AccountAlias] = codec
}

/**
  * Use for dt:string addressing of an account mapped to a single ripple address (e.g. a gateway account)
  * Can be used for anything though
  * @param tag
  */
case class DestinationTag(tag: UInt32)

object DestinationTag {
  def apply(dt: Long): DestinationTag       = DestinationTag(UInt32(dt))
  implicit val codec: Codec[DestinationTag] = deriveUnwrappedCodec[DestinationTag]
}

/**
  *
  * @param tag
  */
case class SourceTag(tag: UInt32)

object SourceTag {
  def apply(dt: Long): SourceTag       = SourceTag(UInt32(dt))
  implicit val codec: Codec[SourceTag] = deriveUnwrappedCodec[SourceTag]
}
