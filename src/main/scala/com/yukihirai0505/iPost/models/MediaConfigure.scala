package com.yukihirai0505.iPost.models

case class MediaConfigure(guid: String, deviceId: String, deviceTimestamp: String, mediaId: String, caption: String, sourceType: String = "5", filter_type: String = "0", extra: Seq[String] = Seq.empty[String])


import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object MediaConfigure {
  implicit val MediaConfigureFormat = JsonNaming.snakecase(Json.format[MediaConfigure])
}