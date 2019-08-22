package com.odenzo.ripple.models.wireprotocol.serverinfo

import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

import com.odenzo.ripple.models.atoms._
import com.odenzo.ripple.models.support.{RippleAdminRq, RippleAdminRs}

case class FeatureRq(id: RippleMsgId = RippleMsgId.random) extends RippleAdminRq {}

// TODO: Enable better results with customer decoder.
/**
  * "07D43DCE529B15A10827E5E04943B496762F9A88E3268269D69C44BE49E21104" : {
  * "enabled" : true,
  * "name" : "Escrow",
  * "supported" : true,
  * "vetoed" : false
  * },
  *
  * @param features
  */
case class FeatureRs(features: List[Amendment]) extends RippleAdminRs

case class Amendment(id: RippleHash, enabled: Boolean, name: String, supported: Boolean, vetoed: Boolean)

object FeatureRq {
  val command: (String, Json) = "command" -> "feature".asJson
  implicit val encoder: Encoder.AsObject[FeatureRq] = {
    deriveEncoder[FeatureRq].mapJsonObject(o => command +: o)
  }
}

object FeatureRs {

  import cats.implicits._

  // Few different appraoches, one is to convert the Json before parsing which I like.

  case class FeatureInfo(enabled: Boolean, name: String, supported: Boolean, vetoed: Boolean)

  implicit val featureDecoder: Decoder[FeatureInfo] = deriveDecoder[FeatureInfo]
  //  /**
//    * "features": {
//    * "07D43DCE529B15A10827E5E04943B496762F9A88E3268269D69C44BE49E21104" : {
//    * "enabled" : true,
//    * "name" : "Escrow",
//    * "supported" : true,
//    * "vetoed" : false
//    * },
//    * ...
//    * }
//    */
  implicit val decoder: Decoder[FeatureRs] = Decoder.instance[FeatureRs] { cursor =>
    val featuresC: ACursor = cursor.downField("features")

    val fieldNames: List[String] = featuresC.keys.map(x => x.toList).getOrElse(List.empty[String])
    val ok: List[Either[DecodingFailure, Amendment]] = fieldNames.map { name =>
      val id = RippleHash(name)
      featuresC
        .get[FeatureInfo](name)
        .map(info => Amendment(id, info.enabled, info.name, info.supported, info.vetoed))
    }

    val ok2: Either[DecodingFailure, List[Amendment]] = fieldNames.traverse { name =>
      val id                        = RippleHash(name)
      val info: Result[FeatureInfo] = featuresC.get[FeatureInfo](name)
      val res: Result[Amendment]    = info.map(info => Amendment(id, info.enabled, info.name, info.supported, info.vetoed))
      res
    }

    val t: Decoder.Result[FeatureRs] = ok2.map(FeatureRs(_))
    t
  }
}

//
//  def convert(field: String): Either[DecodingFailure, Amendment] = {
//    val ans: Either[DecodingFailure, Amendment] = featuresC
//                                                  .get[JsonObject](field)
//                                                  .map(obj => obj.add("id", Json.fromString(field)))
//                                                  .map(_.asJson)
//                                                  .flatMap(_.as[Amendment])
//    ans
//  }
//
//
//}
