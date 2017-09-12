package com.yukihirai0505.iService.common.models

case class Login(username: String, password: String, guid: String, deviceId: String)

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object Login {
  implicit val LoginFormat = JsonNaming.snakecase(Json.format[Login])
}