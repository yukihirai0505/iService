package com.yukihirai0505.iService.services

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iService.common.constans.Methods
import com.yukihirai0505.iService.common.utils.ReqUtil
import com.yukihirai0505.iService.responses.{AccountData, Edges, FollowerData, ProfileUserData}
import dispatch.{Future, Http, Req}
import play.api.libs.json.{JsError, JsSuccess, Json}

import scala.concurrent.ExecutionContext

/**
  * Created by Yuky on 2017/09/25.
  */
object FollowerService {
  def getUserInfo(targetAccountName: String, cookies: List[Cookie])
                 (implicit ec: ExecutionContext): Future[ProfileUserData] = {
    val baseUrl = Methods.Natural.ACCOUNT_URL format targetAccountName
    val req: Req = ReqUtil.getNaturalReq(baseUrl, cookies)
    Http(req).map { resp =>
      val pattern = """<script type="text/javascript">window._sharedData =([\s\S]*?);</script>""".r
      val response = resp.getResponseBody
      if (resp.getStatusCode != 200) throw new Exception(response.toString)
      pattern.findFirstMatchIn(response).fold(throw new RuntimeException("couldn't get user info")) { m =>
        Json.parse(m.group(1)).validate[AccountData] match {
          case JsError(e) => throw new RuntimeException(e.toString())
          case JsSuccess(value, _) =>
            value.entryData.ProfilePage.head.user
        }
      }
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
