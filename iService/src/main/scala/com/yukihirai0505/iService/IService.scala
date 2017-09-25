package com.yukihirai0505.iService

import com.ning.http.client.cookie.Cookie
import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.iService.common.InstagramUser
import com.yukihirai0505.iService.common.constans.Methods
import com.yukihirai0505.iService.responses._
import com.yukihirai0505.iService.services.Follower.{getFollower, getUserInfo}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class IService(username: String, password: String) extends InstagramUser(username, password) with LazyLogging {

  def getFollowers(targetAccountName: String, queryNum: Int = 20): Future[Either[Throwable, Seq[Edges]]] = {
    def execute(cookies: List[Cookie]) = getUserInfo(targetAccountName, cookies).flatMap { userData =>
      logger.info(s"userId: ${userData.id} followedBy: ${userData.followedBy.count}")
      val baseUrl: String = s"${Methods.Natural.FOLLOWER_QUERY(queryNum)}&id=${userData.id}"
      getFollower(baseUrl, cookies).flatMap {
        case Right(nodes) => Future successful Right(nodes)
        case Left(e) => Future successful Left(e)
      }
    }

    commonAction[Seq[Edges]](execute)
  }

  def getSearchHashTagResult(hashTag: String): Future[Either[Throwable, Tag]] = {
    def execute(cookies: List[Cookie]) = services.Media.getPosts(hashTag, cookies).flatMap { tag => Future successful Right(tag) }

    commonAction(execute)
  }

  private def commonAction[T](execute: (List[Cookie]) => Future[Either[Throwable, T]]) = {
    login().flatMap { cookies: List[Cookie] =>
      execute(cookies)
    }.recover { case e: Exception => Left(e) }
  }
}