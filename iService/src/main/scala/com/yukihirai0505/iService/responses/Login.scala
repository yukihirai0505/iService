package com.yukihirai0505.iService.responses

case class Login(username: String, password: String, guid: String, deviceId: String)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object Login {
  implicit val LoginFormat = JsonNaming.snakecase(Json.format[Login])
}