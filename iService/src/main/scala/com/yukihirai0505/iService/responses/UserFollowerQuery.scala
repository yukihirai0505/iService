package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class EdgeFollowedByEdgeNode(username: String, id: String)

object EdgeFollowedByEdgeNode {
  implicit val EdgeFollowedByEdgeNodeFormat = JsonNaming.snakecase(Json.format[EdgeFollowedByEdgeNode])
}

case class EdgeFollowedByEdge(node: EdgeFollowedByEdgeNode)

object EdgeFollowedByEdge {
  implicit val EdgeFollowedByEdgeFormat = JsonNaming.snakecase(Json.format[EdgeFollowedByEdge])
}

case class EdgeFollowedBy(count: Long, edges: Seq[EdgeFollowedByEdge], pageInfo: PageInfo)

object EdgeFollowedBy {
  implicit val EdgeFollowedByFormat = JsonNaming.snakecase(Json.format[EdgeFollowedBy])
}

case class EdgeFollowedByUser(edgeFollowedBy: EdgeFollowedBy)

object EdgeFollowedByUser {
  implicit val EdgeFollowedByUserFormat = JsonNaming.snakecase(Json.format[EdgeFollowedByUser])
}

case class UserFollowerQueryData(user: EdgeFollowedByUser)

object UserFollowerQueryData {
  implicit val UserFollowerQueryDataFormat = JsonNaming.snakecase(Json.format[UserFollowerQueryData])
}

case class UserFollowerQuery(data: UserFollowerQueryData)

object UserFollowerQuery {
  implicit val UserFollowerQueryFormat = JsonNaming.snakecase(Json.format[UserFollowerQuery])
}