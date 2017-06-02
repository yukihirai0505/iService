import java.io.File

import com.yukihirai0505.iPost.iPostNatural
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class iPostNaturalSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iPostNatural = new iPostNatural(username, password)

  "UploadMedia" should "return a Some[MediaFeed]" in {
    val cookies1 = Await.result(iPostNatural.top(), Duration.Inf)
    val cookies2 = Await.result(iPostNatural.login(cookies1), Duration.Inf)
    val cookies3 = Await.result(iPostNatural.top(cookies2), Duration.Inf)
    val uploadPhotoReq = iPostNatural.uploadPhoto(new File("hoge.jpg"), cookies3)
    val uploadId = uploadPhotoReq._2
    val cookies4 = Await.result(uploadPhotoReq._1, Duration.Inf)
    Await.result(iPostNatural.createConfigure(uploadId, "投稿テスト", cookies3), Duration.Inf)
    true should ===(true)
  }

}
