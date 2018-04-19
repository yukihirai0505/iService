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
    val TOP = "https://www.instagram.com/"
    // Login
    val ACCOUNTS_LOGIN_AJAX = s"${TOP}accounts/login/ajax/"
    val CREATE_UPLOAD_PHOTO = s"${TOP}create/upload/photo/"
    val CREATE_CONFIGURE = s"${TOP}create/configure/"
    val CREATE_DELETE_PHOTO: (String) => String = (mediaId: String) => s"${TOP}create/$mediaId/delete/"

    // Like
    val WEB_LIKES_LIKE: (String) => String = (mediaId: String) => s"${TOP}web/likes/$mediaId/like/"

    // User
    val USER_URL = s"$TOP%s/"

    // HashTag
    val HASH_TAG_URL: (String) => String = (hashTag: String) => s"${TOP}explore/tags/$hashTag/"

    // Media
    val MEDIA_URL: (String) => String = (shortcode: String) => s"${TOP}p/$shortcode/"

    // Search
    val SEARCH_API: (String) => String = (query: String) => s"${TOP}web/search/topsearch/?context=blended&query=$query"
  }

  object Graphql {
    private val GRAPHQL = s"${Natural.TOP}graphql/query/?query_hash="
    // memo: if afterCode is empty string, the return object is first result.

    // Like
    val LIKE_QUERY: (String, Int, String) => String = (shortcode: String, size: Int, afterCode: String) =>
      s"${GRAPHQL}17864450716183058&variables=%7B%22shortcode%22%3A%22$shortcode%22%2C%22first%22%3A$size%2C%22after%22%3A%22$afterCode%22%7D"

    // User
    val USER_FOLLOWER_QUERY: (String, Int, String) => String = (userId: String, size: Int, afterCode: String) =>
      s"${GRAPHQL}37479f2b8209594dde7facb0d904896a&variables=%7B%22id%22%3A%22$userId%22%2C%22first%22%3A$size%2C%22after%22%3A%22$afterCode%22%7D"

    val USER_POST_QUERY: (String, Int, String) => String = (userId: String, size: Int, afterCode: String) =>
      s"${GRAPHQL}17888483320059182&variables=%7B%22id%22%3A%22$userId%22%2C%22first%22%3A$size%2C%22after%22%3A%22$afterCode%22%7D"

    // HashTag
    val HASH_TAG_QUERY: (String, Int, String) => String = (tagName: String, size: Int, afterCode: String) =>
      s"${GRAPHQL}17875800862117404&&variables=%7B%22tag_name%22%3A%22$tagName%22%2C%22first%22%3A$size%2C%22after%22%3A%22$afterCode%22%7D"

    // Media
    val MEDIA_COMMENTS_QUERY: (String, Int, String) => String = (shortcode: String, size: Int, afterCode: String) =>
      s"${GRAPHQL}17852405266163336&variables=%7B%22shortcode%22%3A%22$shortcode%22%2C%22first%22%3A$size%2C%22after%22%3A%22$afterCode%22%7D"
  }

}
