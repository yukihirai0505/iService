package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class PageInfo(endCursor: String, hasNextPage: Boolean)

object PageInfo {
  implicit val PageInfoFormat = JsonNaming.snakecase(Json.format[PageInfo])
}

// TODO: parameters
case class MediaNode(caption: Option[String], code: String, id: String, isVideo: Boolean)

object MediaNode {
  implicit val MediaNodeFormat = JsonNaming.snakecase(Json.format[MediaNode])
}

case class Media(nodes: Seq[MediaNode], count: Long, pageInfo: PageInfo)

object Media {
  implicit val MediaFormat = JsonNaming.snakecase(Json.format[Media])
}