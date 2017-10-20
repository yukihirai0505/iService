package com.yukihirai0505.iService.services

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iService.constans.Methods
import com.yukihirai0505.iService.responses.{LikeQuery, LikeQueryShortcodeMedia, Status}
import com.yukihirai0505.iService.utils.ReqUtil
import dispatch.Req

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Yuky on 2017/09/25.
  */
object LikeService extends BaseService {

  def getLikePaging(shortcode: String, size: Int = 20, afterCode: String = "")
                   (implicit ec: ExecutionContext): Future[Either[Throwable, LikeQueryShortcodeMedia]] = {
    val pagingUrl: String = s"${Methods.Graphql.LIKE_QUERY(shortcode, size, afterCode)}"
    logger.info(s"getLikePaging pagingUrl: $pagingUrl")
    val req: Req = ReqUtil.getNaturalReq(pagingUrl)
    Request.sendRequestJson[LikeQuery](req).flatMap {
      case Response(Some(v), _) => Future successful Right(v.data.shortcodeMedia)
      case _ => Future successful Left(throw new RuntimeException("getLikePaging failed"))
    }
  }

  def likeMedia(mediaId: String, shortcode: String, cookies: List[Cookie])
               (implicit ec: ExecutionContext): Future[Either[Throwable, Status]] = {
    val likeUrl: String = s"${Methods.Natural.WEB_LIKES_LIKE(mediaId)}"
    val req: Req = ReqUtil.getNaturalReq(likeUrl, cookies, isAjax = true)
      .setMethod("POST")
      .setHeader("Referer", s"https://www.instagram.com/p/$shortcode/")
    Request.sendRequestJson[Status](req).flatMap {
      case Response(Some(v), _) => Future successful Right(v)
      case _ => Future successful Left(throw new RuntimeException("iLike likeMedia failed"))
    }
  }

}
