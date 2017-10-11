import java.io.File

import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.iService.IService
import com.yukihirai0505.iService.constans.Constants
import com.yukihirai0505.iService.services.MediaService
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class IServiceSpec extends FlatSpec with Matchers with LazyLogging {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iService = new IService(username, password)
  val tagName = "like4like"

  "iFollower" should "get instagram followers" in {
    import java.io.{FileOutputStream => FileStream, OutputStreamWriter => StreamWriter}
    val fileName = "followers.txt"
    val encode = "UTF-8"
    val append = true
    val fileOutPutStream = new FileStream(fileName, append)
    val writer = new StreamWriter(fileOutPutStream, encode)
    // queryNum has limit (until 9999), but 9999 request will cause error. so it is better to set small number as you can.
    val edges = Await.result(iService.getFollowers(targetAccountName = "i_do_not_like_fashion"), Duration.Inf) match {
      case Right(v) =>
        v.foreach(n => writer.write(s"${n.node.username}\n"))
        v
      case Left(e) =>
        logger.warn("failed", e)
        Seq.empty
    }
    writer.close()
    edges.length should be > 0
  }

  "getSearchHashTagResult" should "get hash tag search result" in {
    Await.result(iService.getSearchHashTagResult(hashTag = tagName), Duration.Inf) match {
      case Right(v) =>
        logger.info(s"getSearchHashTagResult hasNextPage: ${v.media.pageInfo.hasNextPage}")
        logger.info(s"getSearchHashTagResult endCursor: ${v.media.pageInfo.endCursor}")
        v.media.nodes.length should be > 0
      case Left(e) =>
        e.getMessage shouldEqual Constants.NOT_FOUND_ERROR_MESSAGE
    }
  }

  "getPostsPaging" should "get hash tag search result paging" in {
    Await.result(MediaService.getPosts(hashTag = tagName), Duration.Inf) match {
      case Right(v) =>
        Await.result(MediaService.getPostsPaging(tagName, v.media.pageInfo.endCursor), Duration.Inf) match {
          case Right(mediaQuery) =>
            mediaQuery.data.hashtag.edgeHashtagToMedia.edges.length should be > 0
          case Left(e) =>
            logger.warn("failed", e)
        }
      case Left(e) =>
        logger.info(e.message)
        e.message.contains("baned") shouldEqual true
    }
  }

  "likeMedia" should "like to media" in {
    val status = Await.result(iService.likeMedia(mediaId = "1611347561905376396", "BZcqBH9D8yM"), Duration.Inf) match {
      case Right(v) => v.status
      case Left(e) =>
        logger.warn("failed", e)
        ""
    }
    status shouldEqual "ok"
  }

  "postNaturalWays" should "post photo to instagram" in {
    val status = Await.result(iService.postNaturalWays(new File("../scripts/hoge.jpg"), "投稿テスト"), Duration.Inf) match {
      case Right(v) =>
        logger.info(s"postNaturalWays result:${v.status} code: ${v.code}")
        v.status
      case Left(e) =>
        logger.warn("failed", e)
        ""
    }
    status shouldEqual "ok"
  }

  "deletePhotos" should "delete photos" in {
    val status = Await.result(iService.getUserInfo(username), Duration.Inf) match {
      case Right(userData) =>
        userData.media.nodes.foreach { n =>
          Await.result(iService.deletePhoto(n.id, n.code), Duration.Inf)
        }
        "ok"
      case Left(e) =>
        logger.warn("failed", e)
        ""
    }
    status shouldEqual "ok"
  }
}
