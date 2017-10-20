package com.yukihirai0505.iService.services

import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iService.constans.Methods
import com.yukihirai0505.iService.responses.MediaCommentQuery
import com.yukihirai0505.iService.utils.ReqUtil
import dispatch.Req

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Yuky on 2017/10/20.
  */
object CommentService extends BaseService {

  def getCommentsPaging(shortcode: String, size: Int = 20, afterCode: String = "")
                       (implicit ec: ExecutionContext): Future[Either[Throwable, MediaCommentQuery]] = {
    val pagingUrl: String = s"${Methods.Graphql.MEDIA_COMMENTS_QUERY(shortcode, size, afterCode)}"
    logger.info(s"getCommentsPaging pagingUrl: $pagingUrl")
    val req: Req = ReqUtil.getNaturalReq(pagingUrl)
    Request.sendRequestJson[MediaCommentQuery](req).flatMap {
      case Response(Some(v), _) => Future successful Right(v)
      case _ => Future successful Left(throw new RuntimeException("getCommentsPaging failed"))
    }
  }
}
