package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class FollowedBy(count: Long)

object FollowedBy {
  implicit val FollowedByFormat = JsonNaming.snakecase(Json.format[FollowedBy])
}

//case class ProfileUserData(id: String, edgeFollowedBy: FollowedBy, edgeOwnerToTimelineMedia: Media)
case class ProfileUserData(id: String, edgeFollowedBy: FollowedBy)

object ProfileUserData {
  implicit val ProfileUserDataFormat = JsonNaming.snakecase(Json.format[ProfileUserData])
}

case class ProfilePageGraphql(user: ProfileUserData)

object ProfilePageGraphql {
  implicit val ProfilePageGraphqlFormat = JsonNaming.snakecase(Json.format[ProfilePageGraphql])
}

case class ProfilePage(graphql: ProfilePageGraphql)

object ProfilePage {
  implicit val ProfilePageFormat = JsonNaming.snakecase(Json.format[ProfilePage])
}

case class UserEntryData(ProfilePage: Seq[ProfilePage])

object UserEntryData {
  implicit val UserEntryDataFormat = JsonNaming.snakecase(Json.format[UserEntryData])
}

case class UserData(entryData: UserEntryData)

object UserData {
  implicit val UserDataFormat = JsonNaming.snakecase(Json.format[UserData])
}
