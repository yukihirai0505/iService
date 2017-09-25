import com.yukihirai0505.iMedia.iMedia
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class iMediaSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iMedia = new iMedia(username, password)

  "iMedia" should "get hash tag search result" in {
    Await.result(iMedia.getSearchHashTagResult(hashTag = "like4like"), Duration.Inf) match {
      case Right(v) => v.media.nodes.foreach(println)
      case Left(e) => println("failed", e)
    }
    true should ===(true)
  }

}
