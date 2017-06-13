package com.yukihirai0505.iPost.responses

import play.api.libs.json.Json

case class Media(code: String)

object Media {
  implicit val MediaFormat = Json.format[Media]
}

case class CreateConfigure(status: String, media: Option[Media])

object CreateConfigure {
  implicit val CreateConfigureFormat = Json.format[CreateConfigure]
}