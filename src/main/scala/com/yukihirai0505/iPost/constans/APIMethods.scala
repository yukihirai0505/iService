package com.yukihirai0505.iPost.constans

/**
  * author Yuki Hirai on 2017/05/30.
  */
object APIMethods {
  private val API_URL = "https://i.instagram.com/api/v1/"
  val ACCOUNTS_LOGIN: String = s"${API_URL}accounts/login/"
  val MEDIA_UPLOAD: String = s"${API_URL}media/upload/"
  val MEDIA_CONFIGURE: String = s"${API_URL}media/configure/"
}
