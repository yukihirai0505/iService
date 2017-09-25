import com.yukihirai0505.iLike.iLike
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class iLikeSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iLike = new iLike(username, password)

  "iLike" should "like to media" in {
    Await.result(iLike.likeMedia(mediaId = "1611347561905376396", "BZcqBH9D8yM"), Duration.Inf) match {
      case Right(v) => println(v.status)
      case Left(e) => println("failed", e)
    }
    true should ===(true)
  }

}
