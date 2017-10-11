package com.yukihirai0505.iService.services

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iService.constans.{Constants, Methods}
import com.yukihirai0505.iService.responses._
import com.yukihirai0505.iService.utils.ReqUtil
import dispatch.Req

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Yuky on 2017/09/25.
  */
object UserService extends BaseService {
  def getUserInfo(targetAccountName: String, cookies: List[Cookie] = List.empty)
                 (implicit ec: ExecutionContext): Future[Either[UserError, ProfileUserData]] = {
    val baseUrl = Methods.Natural.ACCOUNT_URL format targetAccountName
    val req: Req = ReqUtil.getNaturalReq(baseUrl, cookies)
    requestWebPage[AccountData](req).flatMap {
      case Right(v) =>
        v.entryData.ProfilePage.headOption match {
          case Some(p) => Future successful Right(p.user)
          case None => Future successful Left(throw new RuntimeException("no user data"))
        }
      case Left(e) => if (e.getMessage.equals(Constants.NOT_FOUND_ERROR_MESSAGE)) {
        Future successful Left(UserError(s"$targetAccountName is cannot view"))
      } else Future successful Left(UserError(e.getMessage))
    }
  }

  def getFollower(baseUrl: String, cookies: List[Cookie], afterCode: Option[String] = None, nodes: Seq[Edges] = Seq.empty)
                 (implicit ec: ExecutionContext): Future[Either[Throwable, Seq[Edges]]] = {
    val apiUrl = afterCode.fold(baseUrl)(code => s"$baseUrl&after=$code")
    val req: Req = ReqUtil.getNaturalReq(apiUrl, cookies).setMethod("GET")
    Request.sendRequestJson[FollowerData](req).flatMap {
      case Response(Some(v), _) =>
        val userData = v.data.user.edgeFollowedBy
        if (userData.pageInfo.hasNextPage)
          getFollower(
            baseUrl, cookies, Some(userData.pageInfo.endCursor), nodes ++ userData.edges
          )
        else Future successful Right(nodes ++ userData.edges)
      case _ => Future successful Left(throw new RuntimeException("iFollower failed"))
    }
  }
}
