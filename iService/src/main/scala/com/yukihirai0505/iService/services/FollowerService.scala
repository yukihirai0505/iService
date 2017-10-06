package com.yukihirai0505.iService.services

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iService.constans.Methods
import com.yukihirai0505.iService.responses.{AccountData, Edges, FollowerData, ProfileUserData}
import com.yukihirai0505.iService.utils.ReqUtil
import dispatch.{Future, Req}

import scala.concurrent.ExecutionContext

/**
  * Created by Yuky on 2017/09/25.
  */
object FollowerService extends BaseService {
  def getUserInfo(targetAccountName: String, cookies: List[Cookie])
                 (implicit ec: ExecutionContext): Future[ProfileUserData] = {
    val baseUrl = Methods.Natural.ACCOUNT_URL format targetAccountName
    val req: Req = ReqUtil.getNaturalReq(baseUrl, cookies)
    requestWebPage[AccountData](req).flatMap(v => Future successful v.entryData.ProfilePage.head.user)
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
