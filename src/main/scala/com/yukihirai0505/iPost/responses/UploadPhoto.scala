package com.yukihirai0505.iPost.responses

case class UploadPhoto(uploadId: String, status: String)

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object UploadPhoto {
  implicit val UploadPhotoFormat = JsonNaming.snakecase(Json.format[UploadPhoto])
}
