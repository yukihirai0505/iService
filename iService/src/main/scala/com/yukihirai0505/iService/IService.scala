package com.yukihirai0505.iService

import java.io.File

import com.ning.http.client.cookie.Cookie
import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.iService.constans.Methods
import com.yukihirai0505.iService.responses.{Status, _}
import com.yukihirai0505.iService.services.FollowerService.{getFollower, getUserInfo}
import com.yukihirai0505.iService.services.{LikeService, MediaService}
import com.yukihirai0505.iService.utils.NumberUtil

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

    commonAction(execute)
  }

  def getSearchHashTagResult(hashTag: String): Future[Either[Throwable, Tag]] = {
    def execute(cookies: List[Cookie]) = MediaService.getPosts(hashTag, cookies).flatMap { tag => Future successful Right(tag) }

    commonAction(execute)
  }

  def likeMedia(mediaId: String, shortcode: String): Future[Either[Throwable, Status]] = {
    def execute(cookies: List[Cookie]) = LikeService.likeMedia(mediaId, shortcode, cookies)

    commonAction(execute)
  }

  def postNaturalWays(postImage: File, caption: String): Future[Either[Throwable, Status]] = {
    def execute(cookies: List[Cookie]) = MediaService.postNaturalWays(postImage, caption, cookies)

    commonAction(execute)
  }

  def deletePhoto(mediaId: String, shortcode: String): Future[Either[Throwable, Status]] = {
    def execute(cookies: List[Cookie]) = MediaService.deletePhoto(mediaId, shortcode, cookies)

    commonAction(execute)
  }

  private def commonAction[T](execute: (List[Cookie]) => Future[Either[Throwable, T]]) = {
    login().flatMap { cookies: List[Cookie] =>
      Thread.sleep(NumberUtil.getRandomInt())
      execute(cookies)
    }.recover { case e: Exception => Left(e) }
  }
}