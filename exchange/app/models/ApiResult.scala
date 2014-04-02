/**
 * Copyright (C) 2014 Coinport Inc.
 * Author: Chunming Liu (chunming@coinport.com)
 */

package models

case class ApiResult(success: Boolean = true, code: Int = 0, message: String = "", data: Option[Any] = None)

case class SubmitOrderResult(order: UserOrder)

case class UserAccount(uid: Long, accounts: Map[String, Double] = Map())