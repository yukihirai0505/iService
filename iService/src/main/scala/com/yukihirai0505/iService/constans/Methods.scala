package com.yukihirai0505.iService.constans

object Methods {

  object Natural {
    val TOP = "https://www.instagram.com/"
    val ACCOUNTS_LOGIN_AJAX = s"${TOP}accounts/login/ajax/"
    val USER_URL = s"$TOP%s/"
  }

  object Graphql {
    private val GRAPHQL = s"${Natural.TOP}graphql/query/?query_hash="
    // queryNum has limit (until 50),  so it is better to set small number as you can.
    val USER_FOLLOWER_QUERY: (String, Int, String) => String = (userId: String, size: Int, afterCode: String) =>
      s"${GRAPHQL}37479f2b8209594dde7facb0d904896a&variables=%7B%22id%22%3A%22$userId%22%2C%22first%22%3A$size%2C%22after%22%3A%22$afterCode%22%7D"
  }

}
