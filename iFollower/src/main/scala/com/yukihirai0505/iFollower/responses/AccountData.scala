package com.yukihirai0505.iFollower.responses

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming


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

case class EntryData(ProfilePage: Seq[ProfilePage])

object EntryData {
  implicit val EntryDataFormat = JsonNaming.snakecase(Json.format[EntryData])
}

case class AccountData(entryData: EntryData)

object AccountData {
  implicit val AccountDataFormat = JsonNaming.snakecase(Json.format[AccountData])
}
