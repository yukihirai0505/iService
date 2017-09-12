package com.yukihirai0505.iService.common.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import com.yukihirai0505.iService.common.constans.Constants.{HMAC_SHA256, UTF8}

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
    val sha256_HMAC = Mac.getInstance(HMAC_SHA256)
    sha256_HMAC.init(new SecretKeySpec(secret.getBytes(UTF8), HMAC_SHA256))
    sha256_HMAC.doFinal(s.getBytes(UTF8)).map(char => f"$char%02x").mkString
  }

}
