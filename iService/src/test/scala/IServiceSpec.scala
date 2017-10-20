import java.io.File

import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.iService.IService
import com.yukihirai0505.iService.constans.Constants
import com.yukihirai0505.iService.services.{CommentService, MediaService, UserService}
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class IServiceSpec extends FlatSpec with Matchers with LazyLogging {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iService = new IService(username, password)
  val tagName = "idonotlikefashion"
  val targetAccountName = "i_do_not_like_fashion"

  "iFollower" should "get instagram followers" in {
    import java.io.{FileOutputStream => FileStream, OutputStreamWriter => StreamWriter}
    val fileName = "followers.txt"
    val encode = "UTF-8"
    val append = true
    val fileOutPutStream = new FileStream(fileName, append)
    val writer = new StreamWriter(fileOutPutStream, encode)
    // queryNum has limit (until 9999), but 9999 request will cause error. so it is better to set small number as you can.
    val edges = Await.result(iService.getFollowers(targetAccountName = targetAccountName), Duration.Inf) match {
      case Right(v) =>
        v.foreach(n => writer.write(s"${n.node.username}\n"))
        v
      case Left(e) =>
        logger.warn("iFollower failed", e)
        Seq.empty
    }
    writer.close()
    edges.length should be >= 0
  }

  "getSearchHashTagResult" should "get hash tag search result" in {
    Await.result(iService.getSearchHashTagResult(hashTag = tagName), Duration.Inf) match {
      case Right(v) =>
        logger.info(s"getSearchHashTagResult hasNextPage: ${v.media.pageInfo.hasNextPage}")
        logger.info(s"getSearchHashTagResult endCursor: ${v.media.pageInfo.endCursor.getOrElse("nothing")}")
        v.media.nodes.length should be > 0
      case Left(e) =>
        e.getMessage shouldEqual Constants.NOT_FOUND_ERROR_MESSAGE
    }
  }

  "likeMedia" should "like to media" in {
    val status = Await.result(iService.likeMedia(mediaId = "1611347561905376396", "BZcqBH9D8yM"), Duration.Inf) match {
      case Right(v) => v.status
      case Left(e) =>
        logger.warn("likeMedia failed", e)
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
        logger.warn("postNaturalWays failed", e)
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
        logger.warn("deletePhotos failed", e)
        ""
    }
    status shouldEqual "ok"
  }

  "MediaService.getPostsPaging" should "get hash tag search result paging" in {
    Await.result(MediaService.getPosts(hashTag = tagName), Duration.Inf) match {
      case Right(v) =>
        if (v.media.pageInfo.hasNextPage) {
          Await.result(MediaService.getPostsPaging(tagName, size = 9, v.media.pageInfo.endCursor.get), Duration.Inf) match {
            case Right(mediaQuery) =>
              mediaQuery.data.hashtag.edgeHashtagToMedia.edges.length should be > 0
            case Left(e) =>
              logger.warn("MediaService.getPostsPaging failed", e)
          }
        } else logger.info("none next page")
      case Left(e) =>
        logger.info(e.message)
        e.message.contains("baned") shouldEqual true
    }
  }

  "UserService.getPostsPaging" should "get hash tag search result paging" in {
    Await.result(UserService.getUserInfo(targetAccountName = targetAccountName), Duration.Inf) match {
      case Right(v) =>
        if (v.media.pageInfo.hasNextPage) {
          Await.result(UserService.getPostsPaging(v.id, size = 12, v.media.pageInfo.endCursor.get), Duration.Inf) match {
            case Right(postQuery) =>
              postQuery.data.user.edgeOwnerToTimelineMedia.edges.length should be > 0
            case Left(e) =>
              logger.warn("UserService.getPostsPaging failed", e)
          }
        } else logger.info("none next page")
      case Left(e) =>
        logger.info(e.message)
        e.message.contains("baned") shouldEqual true
    }
  }

  "CommentService.getCommentPaging" should "get hash comment result paging" in {
    val shortcode = "BadJrFsh8LK"
    Await.result(MediaService.getPostInfo(shortcode = shortcode), Duration.Inf) match {
      case Right(v) =>
        logger.info("comment counts: ", v.shortcodeMedia.edgeMediaToComment.count)
        val pageInfo = v.shortcodeMedia.edgeMediaToComment.pageInfo
        if (pageInfo.hasNextPage) {
          Await.result(CommentService.getCommentsPaging(shortcode, size = 20, pageInfo.endCursor.get), Duration.Inf) match {
            case Right(commentQuery) =>
              commentQuery.data.shortcodeMedia.edgeMediaToComment.edges.length should be > 0
            case Left(e) =>
              logger.warn("CommentService.getCommentPaging failed", e)
          }
        } else logger.info("none next page")
      case Left(e) =>
        logger.info(e.message)
        e.message.contains("baned") shouldEqual true
    }
  }
}
