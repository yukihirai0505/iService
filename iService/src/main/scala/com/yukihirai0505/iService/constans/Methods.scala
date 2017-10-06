package com.yukihirai0505.iService.constans

/**
  * author Yuki Hirai on 2017/05/30.
  */
object Methods {

  object Private {
    private val API_URL = "https://i.instagram.com/api/v1/"
    val ACCOUNTS_LOGIN: String = s"${API_URL}accounts/login/"
    val MEDIA_UPLOAD: String = s"${API_URL}media/upload/"
    val MEDIA_CONFIGURE: String = s"${API_URL}media/configure/"
  }

  object Natural {
    private val GRAPHQL = "graphql/query/?query_id="
    val TOP = "https://www.instagram.com/"
    val ACCOUNTS_LOGIN_AJAX = s"${TOP}accounts/login/ajax/"
    val CREATE_UPLOAD_PHOTO = s"${TOP}create/upload/photo/"
    val CREATE_CONFIGURE = s"${TOP}create/configure/"
    val FOLLOWER_QUERY: (Int) => String = (queryNum: Int) => s"$TOP${GRAPHQL}17851374694183129&first=$queryNum"
    val WEB_LIKES_LIKE: (String) => String = (mediaId: String) => s"${TOP}web/likes/$mediaId/like/"
    val ACCOUNT_URL = s"$TOP%s/"
    val HASH_TAG_URL: (String) => String = (hashTag: String) => s"${TOP}explore/tags/$hashTag/"
    val HASH_TAG_QUERY: (String, String) => String = (tagName: String, afterCode: String) =>
      s"$TOP${GRAPHQL}17875800862117404&&variables=%7B%22tag_name%22%3A%22$tagName%22%2C%22first%22%3A9%2C%22after%22%3A%22$afterCode%22%7D"
  }
}
