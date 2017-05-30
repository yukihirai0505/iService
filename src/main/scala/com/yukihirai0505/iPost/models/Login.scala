package com.yukihirai0505.iPost.models

case class Login(mediaId: String, status: String)

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object MediaUpload {
  implicit val MediaUploadFormat = JsonNaming.snakecase(Json.format[Login])
}