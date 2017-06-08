package com.yukihirai0505.iPost.responses

case class CreateConfigure(status: String)


import play.api.libs.json.Json

object CreateConfigure {
  implicit val CreateConfigureFormat = Json.format[CreateConfigure]
}