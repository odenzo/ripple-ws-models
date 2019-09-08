package com.odenzo.ripple.models.wireprotocol

import io.circe.syntax._
import io.circe.{Codec, JsonObject, Decoder}
import io.circe.Decoder.Result
object CommandTypeClass {

  /** Experiment #5189 failed again ^_^ */
  trait LikeRippleCommand[A] {
    type RS
    def rqCodec: Codec.AsObject[A]
    def rsCodec: Codec.AsObject[RS]
    def command: String
    def encodeRq(a: A): JsonObject
    def decodeRq(jobj: JsonObject): Decoder.Result[A]
    def encodeRs(b: RS): JsonObject
    def decodeRs(jobj: JsonObject): Decoder.Result[RS]
  }

  object LikeRippleCommand {

    def commandName[A](a: A)(implicit instance: LikeRippleCommand[A]): String             = instance.command
    def requestCodec[A](a: A)(implicit instance: LikeRippleCommand[A]): Codec.AsObject[A] = instance.rqCodec
    def encode[A](a: A)(implicit instance: LikeRippleCommand[A]): Unit = {
      instance.command
    }

    def decodeResponseFor[A](a: Option[A])(implicit instance: LikeRippleCommand[A]): Codec.AsObject[instance.RS] =
      instance.rsCodec
  }

  object LikeRippleCommandInstances {
    import com.odenzo.ripple.models.wireprotocol.commands.publicmethods.accountinfo._

    implicit val accountSet: LikeRippleCommand[AccountChannelsRq] =
      new LikeRippleCommand[AccountChannelsRq] {
        def command = "account_set"
        type RQ = AccountChannelsRq
        type RS = AccountChannelsRs

        def rqCodec: Codec.AsObject[RQ]            = Codec.AsObject[RQ]
        def rsCodec: Codec.AsObject[RS]            = Codec.AsObject[RS]
        def encodeRq(a: RQ): JsonObject            = rqCodec.encodeObject(a)
        def decodeRq(jobj: JsonObject): Result[RQ] = rqCodec.decodeJson(jobj.asJson)
        def encodeRs(b: RS): JsonObject            = rsCodec.encodeObject(b)
        def decodeRs(jobj: JsonObject): Result[RS] = rsCodec.decodeJson(jobj.asJson)

      }

    implicit val accountCurrencies: LikeRippleCommand[AccountCurrenciesRq] =
      new LikeRippleCommand[AccountCurrenciesRq] {
        def command = "account_set"
        type RQ = AccountCurrenciesRq
        type RS = AccountCurrenciesRs

        def rqCodec: Codec.AsObject[RQ]            = Codec.AsObject[RQ]
        def rsCodec: Codec.AsObject[RS]            = Codec.AsObject[RS]
        def encodeRq(a: RQ): JsonObject            = rqCodec.encodeObject(a)
        def decodeRq(jobj: JsonObject): Result[RQ] = rqCodec.decodeJson(jobj.asJson)
        def encodeRs(b: RS): JsonObject            = rsCodec.encodeObject(b)
        def decodeRs(jobj: JsonObject): Result[RS] = rsCodec.decodeJson(jobj.asJson)

      }

  }

  object RippleCmdTypeBindOps {
    implicit class RippleCmdTypeBindOps[A](value: A) {
      def whoami(implicit instance: LikeRippleCommand[A]): String = {
        instance.command
      }

    }
  }

}
