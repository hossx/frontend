/**
 * Copyright (C) 2014 Coinport Inc.
 * Author: Chunming Liu (chunming@coinport.com)
 */

package models

import play.api.libs.json._
import com.coinport.coinex.data._
import com.coinport.coinex.data.Currency._
import com.coinport.coinex.data.Implicits._
import play.api.libs.functional.syntax._
import play.api.libs.json.JsString
import scala.Some

object Implicits {
  implicit def string2Currency(currencyString: String): Currency = {
    currencyString.toUpperCase match {
      case "RMB" => Currency.Rmb
      case "CNY" => Currency.Rmb
      case "BTC" => Currency.Btc
      case "USD" => Currency.Usd
      case _ => null
    }
  }

  import Operation._
  implicit def UserOrder2BackendOrderCommand(order: UserOrder): DoSubmitOrder = {
        order.operation match {
          case Buy =>
           // convert price
            val price = order.price match {
              case Some(value) => Some(1 / value)
              case None => None
            }
            // regard total as quantity
            val quantity = order.total.getOrElse((order.amount.get * order.price.get)).toLong
            val limit = order.amount
            DoSubmitOrder(order.marketSide, Order(order.uid, 0L, quantity , price, limit))
          case Sell =>
            val price = order.price
            // TODO: handle None total or price
            val quantity = order.amount.getOrElse((order.total.get / order.price.get).toLong)
            val limit = order.total match {case Some(total) => Some(total.toLong) case None => None}
            DoSubmitOrder(order.marketSide, Order(order.uid, 0L, quantity, price, limit))
        }
  }

  implicit val resultWrites: Writes[ApiResult] = (
    (JsPath \ "success").write[Boolean] and
      (JsPath \ "code").write[Int] and
      (JsPath \ "message").write[String]
    )(unlift(ApiResult.unapply))

  implicit val userReads: Reads[User] = (
    (JsPath \ "username").read[String] and
      (JsPath \ "password").read[String]
    )(User.apply _)

  implicit val cashAccountWrites = new Writes[CashAccount] {
    def writes(cachAccount: CashAccount) = Json.obj(
      "currency" -> JsString(cachAccount.currency.toString),
      "available" -> cachAccount.available,
      "locked" -> cachAccount.locked
    )
  }

  implicit val queryAccountResultWrites = new Writes[QueryAccountResult] {
    def writes(obj: QueryAccountResult) = Json.obj(
      "uid" -> obj.userAccount.userId,
      "RMB" -> obj.userAccount.cashAccounts.getOrElse(Rmb, CashAccount(Rmb, 0, 0)).available,
      "BTC" -> obj.userAccount.cashAccounts.getOrElse(Btc, CashAccount(Btc, 0, 0)).available,
      "accounts" -> obj.userAccount.cashAccounts.map(_._2)
    )
  }

  implicit val orderWrites = new Writes[Order] {
    def writes(obj: Order) = Json.arr(
      obj.price,
      obj.quantity,
      obj.userId
    )
  }

  implicit val queryMarketResultWrites = new Writes[QueryMarketResult] {
    def writes(obj: QueryMarketResult) = Json.obj(
      "asks" -> obj.orders1,
      "bids" -> obj.orders2.map(_.inversePrice)
    )
  }
}
