package com.yukihirai0505.iService.responses


import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class TextNode(text: String)

object TextNode {
  implicit val TextNodeFormat = JsonNaming.snakecase(Json.format[TextNode])
}

case class CaptionNode(node: TextNode)

object CaptionNode {
  implicit val CaptionNodeFormat = JsonNaming.snakecase(Json.format[CaptionNode])
}

case class EdgeMediaToCaption(edges: Seq[CaptionNode])

object EdgeMediaToCaption {
  implicit val EdgeMediaToCaptionFormat = JsonNaming.snakecase(Json.format[EdgeMediaToCaption])
}

case class EdgeMediaToComment(count: Long)

object EdgeMediaToComment {
  implicit val EdgeMediaToCommentFormat = JsonNaming.snakecase(Json.format[EdgeMediaToComment])
}

case class Dimensions(height: Int, width: Int)

object Dimensions {
  implicit val DimensionsFormat = JsonNaming.snakecase(Json.format[Dimensions])
}

case class EdgeLikedBy(count: Long)

object EdgeLikedBy {
  implicit val EdgeLikedByFormat = JsonNaming.snakecase(Json.format[EdgeLikedBy])
}

case class Owner(id: String)

object Owner {
  implicit val OwnerFormat = JsonNaming.snakecase(Json.format[Owner])
}

case class MediaQueryNode(
                           commentsDisabled: Boolean,
                           id: String,
                           shortcode: String,
                           thumbnailSrc: String,
                           takenAtTimestamp: String,
                           displayUrl: String,
                           isVideo: Boolean,
                           edgeMediaToCaption: EdgeMediaToCaption,
                           edgeMediaToComment: EdgeMediaToComment,
                           dimensions: Dimensions,
                           edgeLikedBy: EdgeLikedBy,
                           owner: Owner
                         )

object MediaQueryNode {
  implicit val MediaQueryNodeFormat = JsonNaming.snakecase(Json.format[MediaQueryNode])
}

case class MediaEdges(node: Seq[MediaQueryNode])

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

case class MediaQueryData(hashtag: Seq[HashTag], status: String)

object MediaQueryData {
  implicit val MediaQueryDataFormat = JsonNaming.snakecase(Json.format[MediaQueryData])
}

case class MediaQuery(data: MediaQueryData)

object MediaQuery {
  implicit val MediaQueryFormat = JsonNaming.snakecase(Json.format[MediaQuery])
}
