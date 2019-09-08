package com.odenzo.ripple.models.wireprotocol.commands

trait RippleRq          extends Serializable with Product
trait RippleRs          extends Serializable with Product
trait RippleAdminRq     extends RippleRq
trait RippleAdminRs     extends RippleRs
trait RippleScrollingRq extends RippleRq
trait RippleScrollingRs extends RippleRs
