package com.yukihirai0505.iPost.utils

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.iPost.constans.Constants.{ANDROID_USER_AGENT, CONTENT_TYPE, IOS_USER_AGENT, UTF8}
import dispatch.{Future, Http, Req, url}

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * author Yuki Hirai on 2017/05/30.
  */
object ReqUtil {


  def getApiReq(requestUrl: String, isFormUrlEncoded: Boolean = true, cookies: List[Cookie] = List.empty[Cookie]): Req = {
    val baseReq = url(requestUrl)
      .setMethod(Verbs.POST.label)
      .addHeader("User-Agent", ANDROID_USER_AGENT)
    val newReq = if (isFormUrlEncoded) baseReq.setContentType(CONTENT_TYPE, UTF8) else baseReq
    addCookies(cookies, newReq)
  }

  def getNaturalReq(requestUrl: String, verbs: Verbs, cookies: List[Cookie] = List.empty[Cookie]): Req = {
    val baseReq = url(requestUrl)
      .setMethod(verbs.label)
      .addHeader("User-Agent", IOS_USER_AGENT)
    addCookies(cookies, baseReq)
  }

  private def addCookies(cookies: List[Cookie], req: Req): Req = {
    if (cookies.isEmpty) req
    else addCookies(cookies.tail, req.addCookie(cookies.head))
  }

  def sendRequest(request: Req): Future[List[Cookie]] = {
    Http(request).map { resp =>
      println(resp.getResponseBody)
      resp.getCookies.toList
    }
  }

}
