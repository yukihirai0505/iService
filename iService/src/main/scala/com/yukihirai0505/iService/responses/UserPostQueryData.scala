package com.yukihirai0505.iService.responses


import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class UserPostEdgesNode(
                           commentsDisabled: Boolean,
                           id: String,
                           shortcode: String,
                           thumbnailSrc: String,
                           takenAtTimestamp: Long,
                           displayUrl: String,
                           isVideo: Boolean,
                           edgeMediaToCaption: EdgeMediaToCaption,
                           edgeMediaToComment: Count,
                           dimensions: Dimensions,
                           edgeMediaPreviewLike: Count,
                           owner: Owner
                         )

object UserPostEdgesNode {
  implicit val UserPostEdgesNodeFormat = JsonNaming.snakecase(Json.format[UserPostEdgesNode])
}

case class UserPostEdges(node: UserPostEdgesNode)

object UserPostEdges {
  implicit val UserPostEdgesFormat = JsonNaming.snakecase(Json.format[UserPostEdges])
}

case class EdgeOwnerToTimelineMedia(count: Long, pageInfo: PageInfo, edges: Seq[UserPostEdges])

object EdgeOwnerToTimelineMedia {
  implicit val EdgeOwnerToTimelineMediaFormat = JsonNaming.snakecase(Json.format[EdgeOwnerToTimelineMedia])
}

case class PostQueryUserData(edgeOwnerToTimelineMedia: EdgeOwnerToTimelineMedia)

object PostQueryUserData {
  implicit val PostQueryUserDataFormat = JsonNaming.snakecase(Json.format[PostQueryUserData])
}

case class UserPostQueryData(user: PostQueryUserData)

object UserPostQueryData {
  implicit val UserPostQueryDataFormat = JsonNaming.snakecase(Json.format[UserPostQueryData])
}

case class UserPostQuery(data: UserPostQueryData, status: String)

object UserPostQuery {
  implicit val UserPostQueryFormat = JsonNaming.snakecase(Json.format[UserPostQuery])
}
