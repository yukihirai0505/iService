package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class UserError(message: String)

case class FollowedBy(count: Long)

object FollowedBy {
  implicit val FollowedByFormat = JsonNaming.snakecase(Json.format[FollowedBy])
}

case class ProfileUserData(id: String, followedBy: FollowedBy, media: Media)

object ProfileUserData {
  implicit val ProfileUserDataFormat = JsonNaming.snakecase(Json.format[ProfileUserData])
}

case class ProfilePage(user: ProfileUserData)

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
