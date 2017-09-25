package com.yukihirai0505.iService.responses

import play.api.libs.json.Json

case class MediaCode(code: String)

object MediaCode {
  implicit val MediaCodeFormat = Json.format[MediaCode]
}

case class CreateConfigure(status: String, media: Option[MediaCode])

object CreateConfigure {
  implicit val CreateConfigureFormat = Json.format[CreateConfigure]
}