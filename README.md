## Examples

```scala
import java.io.File

import com.yukihirai0505.iPost.iPost

import scala.concurrent.ExecutionContext.Implicits.global

val (username, password) = ("Your Instagram Username", sys.env("Your Instagram Password"))
val iPost = new iPost(username, password)
iPost.login().flatMap { cookies =>
  iPost.mediaUpload(new File("image file"), cookies).flatMap { mediaId =>
    iPost.mediaConfigure(mediaId.get, "caption", cookies)
  }
}
```