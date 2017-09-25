package com.yukihirai0505.iService.common.constans

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
    val ACCOUNTS_LOGIN_AJAX = s"${TOP}accounts/login/ajax/"
    val CREATE_UPLOAD_PHOTO = s"${TOP}create/upload/photo/"
    val CREATE_CONFIGURE = s"${TOP}create/configure/"
    val FOLLOWER_QUERY: (Int) => String = (queryNum: Int) => s"${TOP}graphql/query/?query_id=17851374694183129&first=$queryNum"
    val ACCOUNT_URL = s"$TOP%s/"
    val HASH_TAG_URL: (String) => String = (hashTag: String) => s"${TOP}explore/tags/$hashTag/"
  }

}
