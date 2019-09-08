package com.odenzo.ripple.models.atoms

import io.circe.{JsonObject, Decoder}
import io.circe.syntax._

import com.odenzo.ripple.models.support.RippleCodecUtils
import com.odenzo.ripple.models.testkit.CodecTesting
import com.odenzo.ripple.models.utils.CirceUtils
import com.odenzo.ripple.models.utils.caterrors.CatsTransformers.ErrorOr

class RippleKeys$Test extends CodecTesting {

  val realJson =
    """
           {
  "id" : "db3aaf6c-7f70-4cfa-8b8f-52302def036d",
  "result" : {
    "account_id" : "rfy626fL51jjsS9y5tpVsQoeRkTS8iCiPX",
    "key_type" : "secp256k1",
    "master_key" : "HAS LEND HO NOOK DUCK IRON ANNE GLAD GIVE MASS AIDS MIRE",
    "master_seed" : "sh4mtnxr8mXxAufgfmP4GMM9YUxmc",
    "master_seed_hex" : "78A124DC308A314D3E79B7E26C983519",
    "public_key" : "aBPjvPibsh6Rye4FJW5ptHJcj75Q2ZmvtpjpsvYRD7fFmr38bZ3F",
    "public_key_hex" : "02EAD43C359B8597806EEFAC67EB4EC4206C5FA9F6D51390F17C0CF678148202C7"
  },
  "status" : "success",
  "type" : "response"
}
       """.stripMargin

  test("Roundtrip Sample") {
    val json: JsonObject           = testCompleted(parseAsJObj(realJson))
    val keys: ErrorOr[AccountKeys] = RippleCodecUtils.decodeFullyOnSuccess(json, Decoder[AccountKeys])
    logger.info(s"Keys: $keys")
    val bjson: JsonObject = testCompleted(keys).asJsonObject
    logger.info(s"Back to JSON:\n" + bjson.asJson.spaces2)
    val exp = CirceUtils.findObjectField("result", json)
    bjson shouldEqual testCompleted(exp)
  }

}
