package com.yukihirai0505.iPost.utils

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.iPost.constans.Constants.{ANDROID_USER_AGENT, IOS_USER_AGENT, UTF8}
import com.yukihirai0505.iPost.constans.ContentType
import dispatch.{Future, Http, Req}

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * author Yuki Hirai on 2017/05/30.
  */
object ReqUtil {


  def getApiReq(requestUrl: String, isFormUrlEncoded: Boolean = true, cookies: List[Cookie] = List.empty[Cookie]): Req = {
    val baseReq = Request.getBaseReq(requestUrl, cookies)
      .setMethod(Verbs.POST.label)
      .addHeader("User-Agent", ANDROID_USER_AGENT)
    if (isFormUrlEncoded) baseReq.setContentType(ContentType.APPLICATION_X_WWW_FORM_URL_ENCODED, UTF8)
    else baseReq
  }

  def getNaturalReq(requestUrl: String, cookies: List[Cookie] = List.empty[Cookie], isAjax: Boolean = false): Req = {
    val baseReq = Request.getBaseReq(requestUrl, cookies)
      .addHeader("User-Agent", IOS_USER_AGENT)
    val csrfToken = cookies.find(v => v.getName.equals("csrftoken")).map(_.getValue).getOrElse("")
    if (isAjax)
      baseReq
        .addHeader("Host", "www.instagram.com")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("X-Instagram-AJAX", "1")
        .addHeader("Accept", "*/*")
        .addHeader("X-CSRFToken", csrfToken)
        .addHeader("Accept-Encoding", "gzip, deflate, br")
        .addHeader("Accept-Language", "ja,en-US;q=0.8,en;q=0.6")
    else baseReq
  }

  def sendRequest(request: Req): Future[List[Cookie]] = {
    Http(request).map { resp =>
      resp.getCookies.toList
    }
  }

}
