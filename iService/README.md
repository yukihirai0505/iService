To get instagram all followers.

## Setup

sbt

If you don't have it already, make sure you add the Maven Central as resolver in your SBT settings:

You need to include the library as your dependency:

```scala
libraryDependencies += "com.yukihirai0505" % "iservice_2.11" % "1.0.0"
```

https://search.maven.org/#artifactdetails%7Ccom.yukihirai0505%7Ciservice_2.11%7C1.0.0%7Cjar

## Example

Using browser url

### Using scala

```scala
import java.io.File

import com.yukihirai0505.iService.IService
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
val iService = new IService(username, password)


// Get followers
iService.getFollowers(targetAccountName = "[target account name]").flatMap {
  case Right(result) => Future successful result.foreach(n => println(n.node.username))
  case Left(_) => Future successful "failed"
}

// Get hash tag search result
iService.getSearchHashTagResult(hashTag = "[hash tag]").flatMap {
  case Right(v) => Future successful v.media.nodes.foreach(println)
  case Left(e) => Future successful "failed"
}

// Like to media
iService.likeMedia(mediaId = "[media id]", "[shortcode]").flatMap {
  case Right(v) => Future successful println(v.status)
  case Left(e) => Future successful println("failed", e)
}

// Post to timeline
iService.postNaturalWays(new File("path/to/post"), "test").flatMap {
  case Right(v) => Future successful println(s"result:${v.status} code: ${v.code}")
  case Left(e) => Future successful println("failed", e)
}
```