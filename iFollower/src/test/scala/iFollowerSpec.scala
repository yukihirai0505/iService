import com.yukihirai0505.iFollower.iFollower
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class iFollowerSpec extends FlatSpec with Matchers {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iFollower = new iFollower(username, password)

  "iFollower" should "get instagram followers" in {
    import java.io.{ FileOutputStream=>FileStream, OutputStreamWriter=>StreamWriter };
    val fileName = "followers.txt"
    val encode = "UTF-8"
    val append = true
    val fileOutPutStream = new FileStream(fileName, append)
    val writer = new StreamWriter( fileOutPutStream, encode )
    Await.result(iFollower.getFollowers(targetAccountName = "i_do_not_like_holidays"), Duration.Inf) match {
      case Right(v) => v.foreach(n => writer.write(n.node.username))
      case Left(e) => println("failed", e)
    }
    writer.close()
    true should ===(true)
  }

}
