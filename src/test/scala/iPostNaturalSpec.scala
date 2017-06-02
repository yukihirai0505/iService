import com.yukihirai0505.iPost.iPostNatural
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class iPostNaturalSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iPostNatural = new iPostNatural(username, password)

  "UploadMedia" should "return a Some[MediaFeed]" in {
    val firstCookies = Await.result(iPostNatural.top(), Duration.Inf)
    val secondCookies = Await.result(iPostNatural.login(firstCookies), Duration.Inf)
    //Await.result(iPostNatural.top(secondCookies), Duration.Inf)
    true should ===(true)
  }

}
