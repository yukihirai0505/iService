package com.yukihirai0505.iPost.utils

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.iPost.constans.Constants.{CONTENT_TYPE, USER_AGENT, UTF8}
import dispatch.{Req, url}

/**
  * author Yuki Hirai on 2017/05/30.
  */
object ReqUtil {


  def getReq(requestUrl: String, isFormUrlEncoded: Boolean = true, cookies: List[Cookie] = List.empty[Cookie]): Req = {
    val baseReq = url(requestUrl)
      .setMethod(Verbs.POST.label)
      .addHeader("User-Agent", USER_AGENT)
    val newReq = if (isFormUrlEncoded) baseReq.setContentType(CONTENT_TYPE, UTF8) else baseReq
    addCookies(cookies, newReq)
  }

  private def addCookies(cookies: List[Cookie], req: Req): Req = {
    if (cookies.isEmpty) req
    else addCookies(cookies.tail, req.addCookie(cookies.head))
  }

}
