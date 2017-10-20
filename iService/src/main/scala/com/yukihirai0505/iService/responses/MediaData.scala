package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class PostOwner(
                      id: String,
                      profilePicUrl: String,
                      username: String,
                      blockedByViewer: Boolean,
                      followedByViewer: Boolean,
                      fullName: String,
                      hasBlockedViewer: Boolean,
                      isPrivate: Boolean,
                      isUnpublished: Boolean,
                      isVerified: Boolean,
                      requestedByViewer: Boolean
                    )


object PostOwner {
  implicit val PostOwnerFormat = JsonNaming.snakecase(Json.format[PostOwner])
}

case class EdgeMediaPreviewLikeEdge(node: CommentOwner)

object EdgeMediaPreviewLikeEdge {
  implicit val EdgeMediaPreviewLikeEdgeFormat = JsonNaming.snakecase(Json.format[EdgeMediaPreviewLikeEdge])
}

case class EdgeMediaPreviewLike(count: Long, edges: Seq[EdgeMediaPreviewLikeEdge])

object EdgeMediaPreviewLike {
  implicit val EdgeMediaPreviewLikeFormat = JsonNaming.snakecase(Json.format[EdgeMediaPreviewLike])
}

case class EdgeMediaToTaggedUserNodeUser(username: String)

object EdgeMediaToTaggedUserNodeUser {
  implicit val EdgeMediaToTaggedUserNodeUserFormat = JsonNaming.snakecase(Json.format[EdgeMediaToTaggedUserNodeUser])
}

case class EdgeMediaToTaggedUserNode(user: EdgeMediaToTaggedUserNodeUser, x: Double, y: Double)

object EdgeMediaToTaggedUserNode {
  implicit val EdgeMediaToTaggedUserNodeFormat = JsonNaming.snakecase(Json.format[EdgeMediaToTaggedUserNode])
}


case class EdgeMediaToTaggedUserEdge(node: EdgeMediaToTaggedUserNodeUser)

object EdgeMediaToTaggedUserEdge {
  implicit val EdgeMediaToTaggedUserEdgeFormat = JsonNaming.snakecase(Json.format[EdgeMediaToTaggedUserEdge])
}

case class EdgeMediaToTaggedUser(edges: Seq[EdgeMediaToTaggedUserEdge])

object EdgeMediaToTaggedUser {
  implicit val EdgeMediaToTaggedUserFormat = JsonNaming.snakecase(Json.format[EdgeMediaToTaggedUser])
}

case class ShortcodeMedia(
                           id: String,
                           shortcode: String,
                           dimensions: Dimensions,
                           mediaPreview: String,
                           displayUrl: String,
                           isVideo: Boolean,
                           shouldLogClientEvent: Boolean,
                           trackingToken: String,
                           edgeMediaToTaggedUser: EdgeMediaToTaggedUser,
                           captionIsEdited: Boolean,
                           edgeMediaToComment: EdgeMediaToComment,
                           commentsDisabled: Boolean,
                           takenAtTimestamp: Long,
                           edgeMediaPreviewLike: EdgeMediaPreviewLike,
                           owner: PostOwner
                         )

object ShortcodeMedia {
  implicit val ShortcodeMediaFormat = JsonNaming.snakecase(Json.format[ShortcodeMedia])
}

case class PostPageGraphql(shortcodeMedia: ShortcodeMedia)

object PostPageGraphql {
  implicit val PostPageGraphqlFormat = JsonNaming.snakecase(Json.format[PostPageGraphql])
}

case class PostPage(graphql: PostPageGraphql)

object PostPage {
  implicit val PostPageFormat = JsonNaming.snakecase(Json.format[PostPage])
}

case class MediaEntryData(PostPage: Seq[PostPage])

object MediaEntryData {
  implicit val MediaEntryDataFormat = JsonNaming.snakecase(Json.format[MediaEntryData])
}

case class MediaData(entryData: MediaEntryData)

object MediaData {
  implicit val MediaDataFormat = JsonNaming.snakecase(Json.format[MediaData])
}
