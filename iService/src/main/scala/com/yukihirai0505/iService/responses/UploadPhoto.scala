package com.yukihirai0505.iService.responses

case class UploadPhoto(uploadId: String, status: String)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object UploadPhoto {
  implicit val UploadPhotoFormat = JsonNaming.snakecase(Json.format[UploadPhoto])
}
