import java.io.File

import com.yukihirai0505.iService.IService
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class IServiceSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iService = new IService(username, password)

  "iFollower" should "get instagram followers" in {
    import java.io.{FileOutputStream => FileStream, OutputStreamWriter => StreamWriter}
    val fileName = "followers.txt"
    val encode = "UTF-8"
    val append = true
    val fileOutPutStream = new FileStream(fileName, append)
    val writer = new StreamWriter(fileOutPutStream, encode)
    // queryNum has limit (until 9999), but 9999 request will cause error. so it is better to set small number as you can.
    Await.result(iService.getFollowers(targetAccountName = "i_do_not_like_fashion"), Duration.Inf) match {
      case Right(v) => v.foreach(n => writer.write(s"${n.node.username}\n"))
      case Left(e) => println("failed", e)
    }
    writer.close()
    true should ===(true)
  }

  "iMedia" should "get hash tag search result" in {
    Await.result(iService.getSearchHashTagResult(hashTag = "like4like"), Duration.Inf) match {
      case Right(v) =>
        println(s"hasNextPage: ${v.media.pageInfo.hasNextPage}")
        println(s"endCursor: ${v.media.pageInfo.endCursor}")
        v.media.nodes.foreach(println)
      case Left(e) => println("failed", e)
    }
    true should ===(true)
  }

  "iLike" should "like to media" in {
    Await.result(iService.likeMedia(mediaId = "1611347561905376396", "BZcqBH9D8yM"), Duration.Inf) match {
      case Right(v) => println(v.status)
      case Left(e) => println("failed", e)
    }
    true should ===(true)
  }

  "postNaturalWays" should "post photo to instagram" in {
    Await.result(iService.postNaturalWays(new File("../scripts/hoge.jpg"), "投稿テスト"), Duration.Inf) match {
      case Right(v) => println(s"result:${v.status} code: ${v.code}")
      case Left(e) => println("failed", e)
    }
    true should ===(true)
  }

}
