package com.yukihirai0505.iService.responses

case class MediaConfigure(guid: String, deviceId: String, deviceTimestamp: String, mediaId: String, caption: String, sourceType: String = "5", filter_type: String = "0", extra: Seq[String] = Seq.empty[String])


import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object MediaConfigure {
  implicit val MediaConfigureFormat = JsonNaming.snakecase(Json.format[MediaConfigure])
}