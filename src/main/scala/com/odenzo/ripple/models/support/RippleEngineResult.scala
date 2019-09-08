package com.odenzo.ripple.models.support

import io.circe.Codec
import io.circe.generic.semiauto._

/** In case of transactions we have another layer of error messaging within the `result` of RippleGenericResponse
  *  The txn result is a sibling to this stuff, rather than nested like the result in RippleGenericResponse.
  *  This allows both Queued and Success, not that queueing may later fail and probably should be checked seperately.
  *  THought is that in both cases the Transaction is on a path to <em>possible</em> success.
  *
  * @param engine_result
  *  @param engine_result_code       Txn Error Code, this have specific categories, enumerated but not used yet
  *  @param engine_result_message
  */
case class RippleEngineResult(engine_result: String, engine_result_code: Long, engine_result_message: String) {
  def isSuccess: Boolean = engine_result.equals("tesSUCCESS") || engine_result.equals("terQUEUED")
}

object RippleEngineResult {
  implicit val codec: Codec.AsObject[RippleEngineResult] = deriveCodec[RippleEngineResult]
}
