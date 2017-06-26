import com.yukihirai0505.iFollower.iFollower
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class iFollowerSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iFollower = new iFollower(username, password)

  "iFollower" should "get instagram followers" in {
    Await.result(iFollower.getFollowers(targetAccountName = "i_do_not_like_holidays"), Duration.Inf) match {
      case Right(v) => v.foreach(n => println(n.node.username))
      case Left(e) => println("failed", e)
    }
    true should ===(true)
  }

}
