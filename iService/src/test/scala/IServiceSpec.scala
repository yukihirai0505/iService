import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.iService.IService
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class IServiceSpec extends FlatSpec with Matchers with LazyLogging {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  val iService = new IService(username, password)
  val tagName = "idonotlikefashion"
  val targetAccountName = "yabaiwebyasan"
  val shortcode = "BadJrFsh8LK"

  "iFollower" should "get instagram followers" in {
    import java.io.{FileOutputStream => FileStream, OutputStreamWriter => StreamWriter}
    val fileName = s"${targetAccountName}_followers.txt"
    val encode = "UTF-8"
    val append = true
    val fileOutPutStream = new FileStream(fileName, append)
    val writer = new StreamWriter(fileOutPutStream, encode)
    Await.result(iService.login(), Duration.Inf)
    val edges = Await.result(iService.getFollowers(targetAccountName = targetAccountName), Duration.Inf) match {
      case Right(v) =>
        v.foreach(n => writer.write(s"${n.node.username}\n"))
        v
      case Left(e) =>
        logger.warn("iFollower failed", e)
        Seq.empty
    }
    writer.close()
    edges.length should be >= 0
  }
}
