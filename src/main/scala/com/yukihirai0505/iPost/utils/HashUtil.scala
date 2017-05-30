package com.yukihirai0505.iPost.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
  * author Yuki Hirai on 2017/05/30.
  */
object HashUtil {

  /***
    * Create UUID
     * @return
    */
  def createUUID: String = java.util.UUID.randomUUID.toString

  /***
    * Create HmacSHA256 hash
    * @param s
    * @param secret
    * @return
    */
  def hashHmac(s: String, secret: String): String = {
    val sha256_HMAC = Mac.getInstance("HmacSHA256")
    sha256_HMAC.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"))
    sha256_HMAC.doFinal(s.getBytes("UTF-8")).map(char => f"$char%02x").mkString
  }

}
