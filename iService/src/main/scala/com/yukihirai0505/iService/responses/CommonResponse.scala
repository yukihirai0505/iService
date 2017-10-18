package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class PageInfo(endCursor: String, hasNextPage: Boolean)

object PageInfo {
  implicit val PageInfoFormat = JsonNaming.snakecase(Json.format[PageInfo])
}

case class Dimensions(height: Int, width: Int)

object Dimensions {
  implicit val DimensionsFormat = JsonNaming.snakecase(Json.format[Dimensions])
}

case class Owner(id: String)

object Owner {
  implicit val OwnerFormat = JsonNaming.snakecase(Json.format[Owner])
}

case class Count(count: Long)

object Count {
  implicit val CountFormat = JsonNaming.snakecase(Json.format[Count])
}

case class MediaNode(
                      commentsDisabled: Option[Boolean],
                      id: String,
                      dimensions: Dimensions,
                      owner: Owner,
                      thumbnailSrc: String,
                      isVideo: Boolean,
                      code: String,
                      date: Long,
                      displaySrc: String,
                      caption: Option[String],
                      comments: Count,
                      likes: Count,
                      videoViews: Option[Long]
                    )

object MediaNode {
  implicit val MediaNodeFormat = JsonNaming.snakecase(Json.format[MediaNode])
}

case class Media(nodes: Seq[MediaNode], count: Long, pageInfo: PageInfo)

object Media {
  implicit val MediaFormat = JsonNaming.snakecase(Json.format[Media])
}


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