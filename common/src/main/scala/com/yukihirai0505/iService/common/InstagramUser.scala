package com.yukihirai0505.iService.common

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.iService.common.constans.Methods.Natural
import com.yukihirai0505.iService.common.constans.{ContentType, Methods}
import com.yukihirai0505.iService.common.utils.ReqUtil
import dispatch.Future

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * author Yuki Hirai on 2017/06/26.
  */
class InstagramUser(username: String, password: String) {

  def login(): Future[List[Cookie]] = {
    top().flatMap { c1 =>
      loginToInstagram(c1)
    }
  }

  /**
    * Access Top page and get cookie
    *
    * @return
    */
  private def top(cookies: List[Cookie] = List.empty[Cookie], sleepTime: Int = 0): Future[List[Cookie]] = {
    Thread.sleep(sleepTime)
    val req = ReqUtil.getNaturalReq(Natural.TOP, cookies)
    ReqUtil.sendRequest(req)
  }

  /**
    * Login with cookie and user info
    *
    * @param cookies
    * @return
    */
  private def loginToInstagram(cookies: List[Cookie], sleepTime: Int = 3000): Future[List[Cookie]] = {
    Thread.sleep(sleepTime)
    val body = Map(
      "username" -> username,
      "password" -> password
    )
    val req = ReqUtil.getNaturalReq(Methods.Natural.ACCOUNTS_LOGIN_AJAX, cookies, isAjax = true)
      .addHeader("Content-Type", ContentType.APPLICATION_X_WWW_FORM_URL_ENCODED)
      .addHeader("Referer", "https://www.instagram.com/") << body
    ReqUtil.sendRequest(req)
  }

}
