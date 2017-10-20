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

case class HashTagEntryData(TagPage: Seq[TagPage])

object HashTagEntryData {
  implicit val HashTagEntryDataFormat = JsonNaming.snakecase(Json.format[HashTagEntryData])
}

case class HashTagData(entryData: HashTagEntryData)

object HashTagData {
  implicit val HashTagDataFormat = JsonNaming.snakecase(Json.format[HashTagData])
}