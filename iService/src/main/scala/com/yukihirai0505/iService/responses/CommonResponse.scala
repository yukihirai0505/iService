package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class PageInfo(endCursor: Option[String], hasNextPage: Boolean)

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


case class CommentOwner(id: String, profilePicUrl: String, username: String)

object CommentOwner {
  implicit val CommentOwnerFormat = JsonNaming.snakecase(Json.format[CommentOwner])
}

case class CommentEdgesNode(
                             id: String,
                             text: String,
                             createdAt: Long,
                             owner: CommentOwner
                           )

object CommentEdgesNode {
  implicit val CommentEdgesNodeFormat = JsonNaming.snakecase(Json.format[CommentEdgesNode])
}

case class CommentEdges(node: CommentEdgesNode)

object CommentEdges {
  implicit val CommentEdgesFormat = JsonNaming.snakecase(Json.format[CommentEdges])
}

case class EdgeMediaToComment(count: Long, pageInfo: PageInfo, edges: Seq[CommentEdges])

object EdgeMediaToComment {
  implicit val EdgeMediaToCommentFormat = JsonNaming.snakecase(Json.format[EdgeMediaToComment])
}

case class Status(status: String, code: Option[String])

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object Status {
  implicit val StatusFormat = JsonNaming.snakecase(Json.format[Status])
}