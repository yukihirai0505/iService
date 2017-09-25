package com.yukihirai0505.iService.common.models

case class Status(status: String)

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object Status {
  implicit val StatusFormat = JsonNaming.snakecase(Json.format[Status])
}