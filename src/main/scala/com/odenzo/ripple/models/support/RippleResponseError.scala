package com.odenzo.ripple.models.support

/**
  *  This is the top level fields in a standard Ripple response message.
  *  They indicate the request failed. If this is succesful, txn may still have an engine error underneat.
  *  s
  */
case class RippleResponseError(error: String, error_code: Long, error_message: String) //, json:Option[Json]=None)
