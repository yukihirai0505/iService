There are two ways to post photo to instagram.

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

```
sh ./natural_upload.sh
```