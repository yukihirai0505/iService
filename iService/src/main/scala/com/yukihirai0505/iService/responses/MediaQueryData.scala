package com.yukihirai0505.iService.responses

// TODO: hashTagQueryResultをcase classに落とし込み

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

/**
  *           {
            "node": {
              "edge_media_to_comment": {
                "count": 1
              },
              "dimensions": {
                "height": 1080,
                "width": 1080
              },
              "edge_liked_by": {
                "count": 1155
              },
              "owner": {
                "id": "2080636766"
              }
            }
          }
  */


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
