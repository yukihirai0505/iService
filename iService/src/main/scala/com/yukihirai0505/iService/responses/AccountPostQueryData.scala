package com.yukihirai0505.iService.responses


import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class EdgeOwnerToTimelineMedia(count: Long, pageInfo: PageInfo, edges: Seq[MediaEdges])

object EdgeOwnerToTimelineMedia {
  implicit val EdgeOwnerToTimelineMediaFormat = JsonNaming.snakecase(Json.format[EdgeOwnerToTimelineMedia])
}

case class PostQueryUserData(edgeOwnerToTimelineMedia: EdgeOwnerToTimelineMedia)

object PostQueryUserData {
  implicit val PostQueryUserDataFormat = JsonNaming.snakecase(Json.format[PostQueryUserData])
}

case class AccountPostQueryData(user: PostQueryUserData)

object AccountPostQueryData {
  implicit val AccountPostQueryDataFormat = JsonNaming.snakecase(Json.format[AccountPostQueryData])
}

case class AccountPostQuery(data: AccountPostQueryData, status: String)

object AccountPostQuery {
  implicit val AccountPostQueryFormat = JsonNaming.snakecase(Json.format[AccountPostQuery])
}
