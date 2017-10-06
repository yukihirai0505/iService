package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class PageInfo(endCursor: String, hasNextPage: Boolean)

object PageInfo {
  implicit val PageInfoFormat = JsonNaming.snakecase(Json.format[PageInfo])
}