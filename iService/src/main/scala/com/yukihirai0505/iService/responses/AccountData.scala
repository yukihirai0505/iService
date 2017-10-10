package com.yukihirai0505.iService.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json


case class FollowedBy(count: Long)

object FollowedBy {
  implicit val FollowedByFormat = JsonNaming.snakecase(Json.format[FollowedBy])
}

case class ProfileUserData(id: String, followedBy: FollowedBy)

object ProfileUserData {
  implicit val ProfileUserDataFormat = JsonNaming.snakecase(Json.format[ProfileUserData])
}

case class ProfilePage(user: ProfileUserData)

object ProfilePage {
  implicit val ProfilePageFormat = JsonNaming.snakecase(Json.format[ProfilePage])
}

case class AccountEntryData(ProfilePage: Seq[ProfilePage])

object AccountEntryData {
  implicit val AccountEntryDataFormat = JsonNaming.snakecase(Json.format[AccountEntryData])
}

case class AccountData(entryData: AccountEntryData)

object AccountData {
  implicit val AccountDataFormat = JsonNaming.snakecase(Json.format[AccountData])
}
