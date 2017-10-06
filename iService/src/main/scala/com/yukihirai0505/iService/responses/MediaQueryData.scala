package com.yukihirai0505.iService.responses

// TODO: hashTagQueryResultをcase classに落とし込み

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

/**
  *           {
            "node": {
              "comments_disabled": false,
              "id": "1619475816337251693",
              "edge_media_to_caption": {
                "edges": [
                  {
                    "node": {
                      "text": "[\u4eca\u9031\u306e #\u30d9\u30b9\u30c8\u30b9\u30de\u30a4\u30eb ]\n\uff0e\n#\u6a2a\u6d5c\u30d3\u30fc\u30b3\u30eb\u30bb\u30a2\u30fc\u30ba vs #\u6ecb\u8cc0\u30ec\u30a4\u30af\u30b9\u30bf\u30fc\u30ba \uff0e\n\u8a66\u5408\u5f8c\u306e #\u7530\u6e21\u51cc \u9078\u624b\u3068 #\u30cf\u30b7\u30fc\u30e0\u30b5\u30d3\u30fc\u30c8 \u9078\u624b\u306e\u30cf\u30a4\u30bf\u30c3\u30c1\ud83d\ude4c\ud83c\udffb\ud83d\ude06\n\uff0e\n\u6a2a\u6d5c\u30d3\u30fc\u30fb\u30b3\u30eb\u30bb\u30a2\u30fc\u30ba\u304c\u30af\u30e9\u30d6\u6700\u591a\u70b9\u5dee\u8a18\u9332\u3068\u306a\u308b31\u70b9\u5dee\u3092\u3064\u3051\u3066\u52dd\u5229\u3057\u307e\u3057\u305f\ud83c\udfc6\n\uff0e\n#\ud83c\udf39\u2693\ufe0f #\u30cf\u30a4\u30bf\u30c3\u30c1 #\u6700\u591a\u70b9\u5dee\u8a18\u9332 #\u7b11\u9854 #smile #\u30cb\u30b3\u30cb\u30b3 #happy"
                    }
                  }
                ]
              },
              "shortcode": "BZ5iKyzHh1t",
              "edge_media_to_comment": {
                "count": 1
              },
              "taken_at_timestamp": 1507276583,
              "dimensions": {
                "height": 1080,
                "width": 1080
              },
              "display_url": "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/e35/22221216_137987410155509_2784126237969219584_n.jpg",
              "edge_liked_by": {
                "count": 1155
              },
              "owner": {
                "id": "2080636766"
              },
              "thumbnail_src": "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s640x640/sh0.08/e35/22221216_137987410155509_2784126237969219584_n.jpg",
              "thumbnail_resources": [],
              "is_video": false
            }
          }
  * @param caption
  * @param code
  * @param id
  * @param isVideo
  */

// TODO: parameters
case class MediaNode(caption: Option[String], code: String, id: String, isVideo: Boolean)

object MediaNode {
  implicit val MediaNodeFormat = JsonNaming.snakecase(Json.format[MediaQueryNode])
}

case class Mepackage com.yukihirai0505.iService.responses

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
