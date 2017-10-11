import java.io.File

import com.yukihirai0505.iService.IServiceP
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class IServicePSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iServiceP = new IServiceP(username, password)

  /*
  "UploadMedia" should "return a Some[MediaFeed]" in {
    Await.result(iServiceP.post(new File("../scripts/yukihirai.jpeg"), "投稿テスト"), Duration.Inf)
    true should ===(true)
  }*/

}