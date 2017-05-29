package com.yukihirai0505.iPost

import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.constants.Verbs
import dispatch.{Req, url}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object iPost extends App {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))

  def signIn(): Unit = {
    val loginUrl = "https://instagram.com/api/v1/accounts/login/"
    val uuid = java.util.UUID.randomUUID.toString
    val deviceId = s"android-$uuid"
    val params = s"""{"username":"$username","password":"$password","guid":"$uuid","device_id":"$deviceId"}"""
    val body = s"ig_sig_key_version=4&signed_body=${hashHmac(params, "b4a23f5e39b5929e0666ac5de94c89d1618a2916")}.${URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\-", "%2D")}"
    println(body)
    val request: Req = url(loginUrl).setMethod(Verbs.POST.label).setBody(body)
    val effectiveRequest = request
      .addHeader("User-Agent", "Instagram 4.0.0 Android (10/3.3.3; 240; 480x320; samsung; GT-I9220; GT-I9220; smdkc210; en_US)")
      .setContentType("application/x-www-form-urlencoded", "UTF-8")
    Request.send[String](effectiveRequest).flatMap { _ =>
      Future successful "hoge"
    }
  }

  def postImage(): Unit = {

  }

  def configureImage(): Unit = {

  }

  def hashHmac(s: String, secret:String): String = {
    val sha256_HMAC = Mac.getInstance("HmacSHA256")
    sha256_HMAC.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"))
    sha256_HMAC.doFinal(s.getBytes("UTF-8")).map(char=>f"$char%02x").mkString
  }

  signIn()
}
