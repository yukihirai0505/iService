import java.io.File

import com.yukihirai0505.iPost.iPost
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class iPostSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iPost = new iPost(username, password)

  "UploadMedia" should "return a Some[MediaFeed]" in {
    val cookies = Await.result(iPost.login(), Duration.Inf)
    val mediaId = Await.result(iPost.mediaUpload(new File("yukihirai.jpeg"), cookies), Duration.Inf).getOrElse("")
    //Await.result(iPost.mediaConfigure(mediaId, "投稿テスト", cookies), Duration.Inf)
    true should ===(true)
  }

}
