package com.yukihirai0505.iLike

import com.ning.http.client.cookie.Cookie
import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iService.common.InstagramUser
import com.yukihirai0505.iService.common.constans.Methods
import com.yukihirai0505.iService.common.models.Status
import com.yukihirai0505.iService.common.utils.ReqUtil
import dispatch.Req

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class iLike(username: String, password: String) extends InstagramUser(username, password) with LazyLogging {

  def likeMedia(mediaId: String, shortcode: String): Future[Either[Throwable, Status]] = {
    val likeUrl: String = s"${Methods.Natural.WEB_LIKES_LIKE(mediaId)}"
    login().flatMap { cookies: List[Cookie] =>
      val req: Req = ReqUtil.getNaturalReq(likeUrl, cookies, isAjax = true)
        .setMethod("POST")
        .setHeader("Referer", s"https://www.instagram.com/p/$shortcode/")
      Request.sendRequestJson[Status](req).flatMap {
        case Response(Some(v), _) => Future successful Right(v)
        case _ => Future successful Left(throw new RuntimeException("iLike likeMedia failed"))
      }.recover { case e: Exception => Left(e) }
    }
  }
}