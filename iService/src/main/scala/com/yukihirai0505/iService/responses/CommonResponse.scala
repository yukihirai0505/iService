package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class PageInfo(endCursor: Option[String], hasNextPage: Boolean)

object PageInfo {
  implicit val PageInfoFormat = JsonNaming.snakecase(Json.format[PageInfo])
}

case class Owner(id: String)

object Owner {
  implicit val OwnerFormat = JsonNaming.snakecase(Json.format[Owner])
}

case class Count(count: Long)

object Count {
  implicit val CountFormat = JsonNaming.snakecase(Json.format[Count])
}

case class Status(status: String, code: Option[String])

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object Status {
  implicit val StatusFormat = JsonNaming.snakecase(Json.format[Status])
}