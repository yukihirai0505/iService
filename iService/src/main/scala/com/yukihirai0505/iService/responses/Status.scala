package com.yukihirai0505.iService.responses

case class Status(status: String, code: Option[String])

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object Status {
  implicit val StatusFormat = JsonNaming.snakecase(Json.format[Status])
}