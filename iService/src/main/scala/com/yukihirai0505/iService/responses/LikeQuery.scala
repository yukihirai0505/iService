package com.yukihirai0505.iService.responses


import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json


case class EdgeLikedByEdgeNode(
                                id: String,
                                username: String,
                                fullName: String,
                                profilePicUrl: String,
                                isVerified: Boolean,
                                followedByViewer: Boolean,
                                requestedByViewer: Boolean
                              )

object EdgeLikedByEdgeNode {
  implicit val EdgeLikedByEdgeNodeFormat = JsonNaming.snakecase(Json.format[EdgeLikedByEdgeNode])
}

case class EdgeLikedByEdge(node: EdgeLikedByEdgeNode)

object EdgeLikedByEdge {
  implicit val EdgeLikedByEdgeFormat = JsonNaming.snakecase(Json.format[EdgeLikedByEdge])
}

case class EdgeLikedBy(count: Long, pageInfo: PageInfo, edges: Seq[EdgeLikedByEdge])

object EdgeLikedBy {
  implicit val EdgeLikedByFormat = JsonNaming.snakecase(Json.format[EdgeLikedBy])
}

case class LikeQueryShortcodeMedia(
                                    id: String,
                                    shortcode: String,
                                    edgeLikedBy: EdgeLikedBy
                                  )

object LikeQueryShortcodeMedia {
  implicit val LikeQueryShortcodeMediaFormat = JsonNaming.snakecase(Json.format[LikeQueryShortcodeMedia])
}

case class LikeQueryData(shortcodeMedia: LikeQueryShortcodeMedia)

object LikeQueryData {
  implicit val LikeQueryDataFormat = JsonNaming.snakecase(Json.format[LikeQueryData])
}

case class LikeQuery(data: LikeQueryData, status: String)

object LikeQuery {
  implicit val LikeQueryFormat = JsonNaming.snakecase(Json.format[LikeQuery])
}
