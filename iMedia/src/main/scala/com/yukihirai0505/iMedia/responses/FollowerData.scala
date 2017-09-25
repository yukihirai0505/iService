package com.yukihirai0505.iFollower.responses

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming


case class PageInfo(endCursor: String, hasNextPage: Boolean)

object PageInfo {
  implicit val PageInfoFormat = JsonNaming.snakecase(Json.format[PageInfo])
}

case class NodeUser(username: String, id: String)

object NodeUser {
  implicit val NodeUserFormat = JsonNaming.snakecase(Json.format[NodeUser])
}

case class Node(node: NodeUser)

object Node {
  implicit val NodeFormat = JsonNaming.snakecase(Json.format[Node])
}

case class EdgeFollowedBy(count: Long, edges: Seq[Node], pageInfo: PageInfo)

object EdgeFollowedBy {
  implicit val EdgeFollowedByFormat = JsonNaming.snakecase(Json.format[EdgeFollowedBy])
}

case class EdgeFollowedByUser(edgeFollowedBy: EdgeFollowedBy)

object EdgeFollowedByUser {
  implicit val EdgeFollowedByUserFormat = JsonNaming.snakecase(Json.format[EdgeFollowedByUser])
}

case class UserData(user: EdgeFollowedByUser)

object UserData {
  implicit val UserDataFormat = JsonNaming.snakecase(Json.format[UserData])
}

case class FollowerData(data: UserData)

object FollowerData {
  implicit val FollowerDataFormat = JsonNaming.snakecase(Json.format[FollowerData])
}