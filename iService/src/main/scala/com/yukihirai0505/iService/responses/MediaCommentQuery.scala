package com.yukihirai0505.iService.responses


import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json


case class ShortMedia(edgeMediaToComment: EdgeMediaToComment)

object ShortMedia {
  implicit val ShortMediaFormat = JsonNaming.snakecase(Json.format[ShortMedia])
}

case class MediaCommentQueryData(shortcodeMedia: ShortMedia)

object MediaCommentQueryData {
  implicit val MediaCommentQueryDataFormat = JsonNaming.snakecase(Json.format[MediaCommentQueryData])
}

case class MediaCommentQuery(data: MediaCommentQueryData, status: String)

object MediaCommentQuery {
  implicit val MediaCommentQueryFormat = JsonNaming.snakecase(Json.format[MediaCommentQuery])
}
