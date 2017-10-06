package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class NodeUser(username: String, id: String)

object NodeUser {
  implicit val NodeUserFormat = JsonNaming.snakecase(Json.format[NodeUser])
}

case class Edges(node: NodeUser)

object Edges {
  implicit val EdgesFormat = JsonNaming.snakecase(Json.format[Edges])
}

case class EdgeFollowedBy(count: Long, edges: Seq[Edges], pageInfo: PageInfo)

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