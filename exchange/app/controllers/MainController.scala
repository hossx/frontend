/**
 * Copyright (C) 2014 Coinport Inc.
 * Author: Chunming Liu (chunming@coinport.com)
 */

package controllers

import play.api.mvc._
import play.api.libs.iteratee.Enumerator
import scala.concurrent.ExecutionContext.Implicits.global
import com.coinport.coinex.api.model._
import com.github.tototoshi.play2.json4s.native.Json4s
import utils.HdfsAccess
import com.coinport.coinex.api.service.NotificationService
import models.PagingWrapper

object MainController extends Controller with Json4s {
  def index = Action {
    implicit request =>
      Ok(views.html.index.render(session, lang))
  }

  def trade = Action {
    implicit request =>
      session.get("uid").map {
        uid =>
          Ok(views.html.trade.render(session, lang))
      } getOrElse {
        Redirect(routes.MainController.login())
      }
  }

  def account() = Action {
    implicit request =>
      Ok(views.html.account.render(session, lang))
  }

  def market = Action {
    implicit request =>
      Ok(views.html.market.render(session, lang))
  }

  def user(uid: String) = Action {
    implicit request =>
      Ok(views.html.user.render(uid, session, lang))
  }

  def order(oid: String) = Action {
    implicit request =>
      Ok(views.html.order.render(oid, session, lang))
  }

  def transaction(tid: String) = Action {
    implicit request =>
      Ok(views.html.transaction.render(tid, session, lang))
  }

  def transactions(market: String) = Action {
    implicit request =>
      Ok(views.html.transactions.render(market, session, lang))
  }

  def depth(market: String) = Action {
    implicit request =>
      Ok(views.html.depth.render(market, session, lang))
  }

  def login(showMsg: Boolean = false, msgKey: String = "") = Action {
    implicit request =>
      Ok(views.html.login.render(showMsg, msgKey, session, lang))
  }

  def register = Action {
    implicit request =>
      Ok(views.html.register.render(session, lang))
  }

  def open = Action {
    implicit request =>
      Ok(views.html.open.render(session, lang))
  }

  def prompt(msgKey: String) = Action {
    implicit request =>
    Ok(views.html.prompt.render(msgKey, lang))
  }

  def downloadFromHdfs(path: String, filename: String) = Action {
    val stream = HdfsAccess.getFileStream(path, filename)
    val fileContent: Enumerator[Array[Byte]] = Enumerator.fromStream(stream)
      .onDoneEnumerating {
      stream.close()
    }

    Result(
      header = ResponseHeader(200),
      body = fileContent
    ).withHeaders("Content-type" -> "application/force-download", "Content-Disposition" -> "attachment")
  }

  def listFilesFromHdfs(path: String) = Action {
    implicit request =>
      val pager = ControllerHelper.parsePagingParam()
      val files = HdfsAccess.listFiles(path)
        .sortWith((a, b) => a.updated > b.updated)

      val from = Math.min(pager.skip, files.length - 1)
      val until = pager.skip + pager.limit

      val items = files.slice(from, until)

      val data = PagingWrapper(
        count = files.length,
        skip = pager.skip,
        limit = pager.limit,
        currentPage = pager.page,
        pageSize = pager.limit,
        items = items)

      val result = ApiResult(data = Some(data))

      Ok(result.toJson)
  }

  def getNotifications() = Action {
    Ok(NotificationService.getNotifications().toJson)
  }

  def bidaskView() = Action {
    implicit request =>
      Ok(views.html.viewBidask.render(lang))
  }

  def registerView() = Action {
    implicit request =>
      Ok(views.html.viewRegister.render(lang))
  }

  def accountSettingsView() = Action {
    implicit request =>
      Ok(views.html.viewAccountSettings.render(lang))
  }

  def assetView() = Action {
    implicit request =>
      Ok(views.html.viewAsset.render(lang))
  }

  def depositView() = Action {
    implicit request =>
      Ok(views.html.viewDeposit.render(lang))
  }

  def withdrawalView() = Action {
    implicit request =>
      Ok(views.html.viewWithdrawal.render(lang))
  }

  def ordersView() = Action {
    implicit request =>
      Ok(views.html.viewOrders.render(lang))
  }

  def transactionsView() = Action {
    implicit request =>
      Ok(views.html.viewTransactions.render(lang))
  }

  def downloadView() = Action {
    implicit request =>
      Ok(views.html.viewDownload.render(lang))
  }

  def reserveView() = Action {
    implicit request =>
      Ok(views.html.viewReserve.render(lang))
  }

  def codeView() = Action {
    implicit request =>
      Ok(views.html.viewCode.render(lang))
  }
}
