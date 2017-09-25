package com.yukihirai0505.iMedia.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

// TODO: parameters
case class Node(caption: String, code: String, id: String, isVideo: Boolean)

object Node {
  implicit val NodeFormat = JsonNaming.snakecase(Json.format[Node])
}

case class Media(nodes: Seq[Node], count: Long)

object Media {
  implicit val ShortcodeMediaFormat = JsonNaming.snakecase(Json.format[Media])
}

case class TopPosts(nodes: Seq[Node])

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

case class EntryData(TagPage: Seq[TagPage])

object EntryData {
  implicit val EntryDataFormat = JsonNaming.snakecase(Json.format[EntryData])
}

case class MediaData(entryData: EntryData)

object MediaData {
  implicit val MediaDataFormat = JsonNaming.snakecase(Json.format[MediaData])
}
