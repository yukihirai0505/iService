package com.yukihirai0505.iFollower

import play.api.libs.json.{JsError, JsSuccess, Json}

import com.ning.http.client.cookie.Cookie
import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iFollower.responses.{AccountData, FollowerData, Node, ProfileUserData}
import com.yukihirai0505.iService.common.InstagramUser
import com.yukihirai0505.iService.common.constans.Methods
import com.yukihirai0505.iService.common.utils.ReqUtil
import dispatch.{Future, Http, Req}

import scala.concurrent.ExecutionContext.Implicits.global

class iFollower(username: String, password: String) extends InstagramUser(username, password) with LazyLogging {

  def getFollowers(targetAccountName: String, queryNum: Int = 20): Future[Either[Throwable, Seq[Node]]] = {
    def getUserInfo(targetAccountName: String, cookies: List[Cookie]): Future[ProfileUserData] = {
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

    def getFollower(baseUrl: String, cookies: List[Cookie], afterCode: Option[String] = None, nodes: Seq[Node] = Seq.empty): Future[Either[Throwable, Seq[Node]]] = {
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

    login().flatMap { cookies: List[Cookie] =>
      getUserInfo(targetAccountName, cookies).flatMap { userData =>
        logger.info(s"userId: ${userData.id} followedBy: ${userData.followedBy.count}")
        val baseUrl: String = s"${Methods.Natural.FOLLOWER_QUERY(queryNum)}&id=${userData.id}"
        getFollower(baseUrl, cookies).flatMap {
          case Right(nodes) => Future successful Right(nodes)
          case Left(e) => Future successful Left(e)
        }
      }
    }.recover { case e: Exception => Left(e) }
  }
}