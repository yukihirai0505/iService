package com.yukihirai0505.iService.responses

case class DeletePhoto(status: String, didDelete: Boolean)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object DeletePhoto {
  implicit val DeletePhotoFormat = JsonNaming.snakecase(Json.format[DeletePhoto])
}