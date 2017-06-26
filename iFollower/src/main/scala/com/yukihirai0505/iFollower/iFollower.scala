package com.yukihirai0505.iFollower

import play.api.libs.json.{JsError, JsSuccess, Json}

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.common.User
import com.yukihirai0505.common.constans.Methods
import com.yukihirai0505.common.utils.ReqUtil
import com.yukihirai0505.iFollower.responses.{AccountData, FollowerData, Node}
import dispatch.{Future, Http, Req}

import scala.concurrent.ExecutionContext.Implicits.global

class iFollower(user: User) {

  def getFollowers(targetAccountName: String): Future[Either[Throwable, Seq[Node]]] = {
    user.login().flatMap { c =>
      getId(targetAccountName).flatMap { targetId =>
        getFollower(cookies = c, id = targetId).flatMap {
          case Right(nodes) => Future successful Right(nodes)
          case Left(e) => Future successful Left(e)
        }
      }
    }.recover { case e: Exception => Left(e) }
  }

  private def getFollower(cookies: List[Cookie], id: String, afterCode: Option[String] = None, nodes: Seq[Node] = Seq.empty): Future[Either[Throwable, Seq[Node]]] = {
    val baseUrl = s"${Methods.Natural.FOLLOWER_QUERY}&id=$id"
    val apiUrl = afterCode.fold(baseUrl)(code => s"$baseUrl&after=$code")
    val req: Req = ReqUtil.getNaturalReq(apiUrl, cookies).setMethod("GET")
    Request.sendRequestJson[FollowerData](req).flatMap {
      case Response(Some(v), _) =>
        val userData = v.data.user.edgeFollowedBy
        if (userData.pageInfo.hasNextPage)
          getFollower(
            cookies, id, Some(userData.pageInfo.endCursor), nodes ++ userData.edges
          )
        else Future successful Right(nodes ++ userData.edges)
      case _ => Future successful Left(throw new RuntimeException("iFollower failed"))
    }
  }

  private def getId(targetAccountName: String): Future[String] = {
    val baseUrl = Methods.Natural.ACCOUNT_URL format targetAccountName
    val req: Req = ReqUtil.getNaturalReq(baseUrl)
    Http(req).map { resp =>
      val pattern = """<script type="text/javascript">window._sharedData =([\s\S]*?);</script>""".r
      val response = resp.getResponseBody
      if (resp.getStatusCode != 200) throw new Exception(response.toString)
      pattern.findFirstMatchIn(response).fold("") { m =>
        Json.parse(m.group(1)).validate[AccountData] match {
          case JsError(e) => throw new Exception(e.toString())
          case JsSuccess(value, _) =>
            value.entryData.ProfilePage.head.user.id
        }
      }
    }
  }
}