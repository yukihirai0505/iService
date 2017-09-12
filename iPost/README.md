There are two ways to post photo to instagram.

## Setup

sbt

If you don't have it already, make sure you add the Maven Central as resolver in your SBT settings:

You need to include the library as your dependency:

```scala
libraryDependencies += "com.yukihirai0505" % "ipost_2.11" % "2.0"
```

https://search.maven.org/#artifactdetails%7Ccom.yukihirai0505%7Cipost_2.11%7C1.6%7Cjar

## Example1

Using private API.


```scala
import java.io.File

import com.yukihirai0505.iPost.iPost

import scala.concurrent.ExecutionContext.Implicits.global

val (username, password) = ("Your Instagram Username", "Your Instagram Password")
val iPost = new iPost(username, password)
iPost.login().flatMap { cookies =>
  iPost.mediaUpload(new File("image file"), cookies).flatMap { mediaId =>
    iPost.mediaConfigure(mediaId.get, "caption", cookies)
  }
}
```

## Example2

Using browser url


### Using Shell script

```
sh ./natural_upload.sh
```

### Using scala

```scala
import java.io.File

import com.yukihirai0505.iPost.iPostNatural

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
val iPostNatural = new iPostNatural(username, password)

iPostNatural.postNaturalWays(new File("hoge.jpg"), "投稿テスト").flatMap {
  case Right(result) => Future successful (if (result.status) "posted" else "failed")
  case Left(_) => Future successful "failed"
}

```