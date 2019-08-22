package com.odenzo.ripple.models.support

/** TODO: This should incorporate the underlying Error classes which I have lost.
  * And found again in errors/TxnStatusCodes pasted in.
  * TxnStatusCode should be used for TxnEngine.
  * Use this for top level and txn_engine?
  **/
case class RippleResponseError(error: String, error_code: Long, error_message: String) //, json:Option[Json]=None)
