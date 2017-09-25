To get instagram all followers.

## Setup

sbt

If you don't have it already, make sure you add the Maven Central as resolver in your SBT settings:

You need to include the library as your dependency:

```scala
libraryDependencies += "com.yukihirai0505" % "ifollower_2.11" % "2.1"
```

https://search.maven.org/#artifactdetails%7Ccom.yukihirai0505%7Cifollower_2.11%7C2.1%7Cjar

## Example

Using browser url

### Using scala

```scala
import java.io.File

import com.yukihirai0505.iFollower.iFollower

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
val iFollower = new iFollower(username, password)

iFollower.getFollowers(targetAccountName = "i_do_not_like_holidays").flatMap {
  case Right(result) => Future successful result.foreach(n => println(n.node.username))
  case Left(_) => Future successful "failed"
}
```