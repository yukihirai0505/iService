package com.yukihirai0505.iService.responses

case class MediaUpload(mediaId: String, status: String)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object MediaUpload {
  implicit val MediaUploadFormat = JsonNaming.snakecase(Json.format[MediaUpload])
}