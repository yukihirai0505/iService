package com.yukihirai0505.iPost.utils

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.iPost.constans.Constants.userAgent
import dispatch.{Req, url}

/**
  * author Yuki Hirai on 2017/05/30.
  */
object ReqUtil {


  def getReq(requestUrl: String, isFormUrlEncoded: Boolean = true): Req = {
    val req = url(requestUrl)
      .setMethod(Verbs.POST.label)
      .addHeader("User-Agent", userAgent)
    if (isFormUrlEncoded) req.setContentType("application/x-www-form-urlencoded", "UTF-8") else req
  }

  def addCookies(cookies: List[Cookie], req: Req): Req = {
    if (cookies.isEmpty) req
    else addCookies(cookies.tail, req.addCookie(cookies.head))
  }

}
