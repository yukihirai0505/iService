package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class TopPosts(nodes: Seq[MediaNode])

object TopPosts {
  implicit val TopPostsFormat = JsonNaming.snakecase(Json.format[TopPosts])
}

case class Tag(media: Media, topPosts: TopPosts)

object Tag {
  implicit val TagFormat = JsonNaming.snakecase(Json.format[Tag])
}

case class TagPage(tag: Tag)

object TagPage {
  implicit val TagPageFormat = JsonNaming.snakecase(Json.format[TagPage])
}

case class MediaEntryData(TagPage: Seq[TagPage])

object MediaEntryData {
  implicit val MediaEntryDataFormat = JsonNaming.snakecase(Json.format[MediaEntryData])
}

case class MediaData(entryData: MediaEntryData)

object MediaData {
  implicit val MediaDataFormat = JsonNaming.snakecase(Json.format[MediaData])
}