package com.yukihirai0505.iService

import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.iService.constans.Methods.Natural
import com.yukihirai0505.iService.constans.{Constants, ContentType, Methods}
import com.yukihirai0505.iService.responses._
import dispatch.Req

import scala.concurrent.{ExecutionContext, Future}

class IService(username: String, password: String)
              (implicit ec: ExecutionContext) extends LazyLogging with BaseService {

  def login(): Future[String] = {
    def top() = {
      val req = getNaturalReq(Natural.TOP)
      sendRequest(req)
    }

    top().flatMap { _ =>
      val body = Map(
        "username" -> username,
        "password" -> password
      )
      val req = getNaturalReq(Methods.Natural.ACCOUNTS_LOGIN_AJAX, isAjax = true)
        .addHeader("Content-Type", ContentType.APPLICATION_X_WWW_FORM_URL_ENCODED)
        .addHeader("Referer", "https://www.instagram.com/") << body
      sendRequest(req)
    }
  }

  def getUserInfo(targetAccountName: String): Future[Either[Throwable, ProfileUserData]] = {
    val baseUrl = Methods.Natural.USER_URL format targetAccountName
    val req: Req = getNaturalReq(baseUrl)
    requestWebPage[UserData](req).flatMap {
      case Right(v) =>
        v.entryData.ProfilePage.headOption match {
          case Some(p) => Future successful Right(p.graphql.user)
          case None => Future successful Left(throw new RuntimeException("no user data"))
        }
      case Left(e) => if (e.getMessage.equals(Constants.NOT_FOUND_ERROR_MESSAGE)) {
        Future successful Left(throw new Exception(s"$targetAccountName is cannot view"))
      } else Future successful Left(throw new Exception(e.getMessage))
    }
  }

  def getFollowers(targetAccountName: String, queryNum: Int = 50): Future[Either[Throwable, Seq[EdgeFollowedByEdge]]] = {

    def getFollower(userId: String, queryNum: Int, afterCode: String = "", nodes: Seq[EdgeFollowedByEdge] = Seq.empty)
                   (implicit ec: ExecutionContext): Future[Either[Throwable, Seq[EdgeFollowedByEdge]]] = {
      val apiUrl: String = s"${Methods.Graphql.USER_FOLLOWER_QUERY(userId, queryNum, afterCode)}"
      logger.info(s"getFollower url: $apiUrl")
      val req: Req = getNaturalReq(apiUrl).setMethod("GET")
      sendRequestJson[UserFollowerQuery](req).flatMap {
        case Response(Some(v), _) =>
          val userData = v.data.user.edgeFollowedBy
          if (userData.pageInfo.hasNextPage)
            getFollower(
              userId, queryNum, userData.pageInfo.endCursor.get, nodes ++ userData.edges
            )
          else Future successful Right(nodes ++ userData.edges)
        case _ => Future successful Left(throw new RuntimeException("iFollower failed"))
      }
    }

    getUserInfo(targetAccountName).flatMap {
      case Right(userData) =>
        logger.info(s"getFollowers userId: ${userData.id} followedBy: ${userData.edgeFollowedBy.count}")
        if (userData.edgeFollowedBy.count > 0) {
          getFollower(userData.id, queryNum).flatMap {
            case Right(nodes) => Future successful Right(nodes)
            case Left(e) => Future successful Left(e)
          }
        } else Future successful Right(Seq.empty)
      case Left(e) => Future successful Left(e)
    }
  }

}