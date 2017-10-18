package com.yukihirai0505.iService.responses


import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class MediaQueryNode(
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
                           edgeLikedBy: Count,
                           owner: Owner
                         )

object MediaQueryNode {
  implicit val MediaQueryNodeFormat = JsonNaming.snakecase(Json.format[MediaQueryNode])
}

case class MediaEdges(node: MediaQueryNode)

object MediaEdges {
  implicit val MediaEdgesFormat = JsonNaming.snakecase(Json.format[MediaEdges])
}

case class EdgeHashtagToMedia(count: Long, pageInfo: PageInfo, edges: Seq[MediaEdges])

object EdgeHashtagToMedia {
  implicit val EdgeHashtagToMediaFormat = JsonNaming.snakecase(Json.format[EdgeHashtagToMedia])
}

case class HashTag(name: String, edgeHashtagToMedia: EdgeHashtagToMedia)

object HashTag {
  implicit val HashTagFormat = JsonNaming.snakecase(Json.format[HashTag])
}

case class MediaQueryData(hashtag: HashTag)

object MediaQueryData {
  implicit val MediaQueryDataFormat = JsonNaming.snakecase(Json.format[MediaQueryData])
}

case class MediaQuery(data: MediaQueryData, status: String)

object MediaQuery {
  implicit val MediaQueryFormat = JsonNaming.snakecase(Json.format[MediaQuery])
}
