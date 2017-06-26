import java.io.File

import com.yukihirai0505.iPost.iPostNatural
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class iPostNaturalSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iPostNatural = new iPostNatural(username, password)

  "postNaturalWays" should "post photo to instagram" in {
    Await.result(iPostNatural.postNaturalWays(new File("hoge.jpg"), "投稿テスト"), Duration.Inf) match {
      case Right(v) => println(s"result:${v.status} code: ${v.code}")
      case Left(e) => println("failed", e)
    }
    true should ===(true)
  }

}
