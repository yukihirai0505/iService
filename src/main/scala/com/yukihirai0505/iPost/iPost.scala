package com.yukihirai0505.iPost

import java.io.File
import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import play.api.libs.json.Reads

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.com.scala.constants.Verbs
import dispatch.{Future, Http, Req, url}

import scala.collection.JavaConversions._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object iPost extends App {

  val (username, password) = (sys.env("INSTAGRAM_USERNAME"), sys.env("INSTAGRAM_PASSWORD"))
  var cookies: List[Cookie] = List.empty

  def signIn(): Future[Option[String]] = {
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
    send[String](effectiveRequest)
  }

  def postImage(postImage: File): Unit = {
    val uploadUrl = "https://instagram.com/api/v1/media/upload/"
    val request: Req = url(uploadUrl)
      .setMethod(Verbs.POST.label)
      .addHeader("User-Agent", "Instagram 4.0.0 Android (10/3.3.3; 240; 480x320; samsung; GT-I9220; GT-I9220; smdkc210; en_US)")
      .addBodyPart(new FilePart("photo", postImage))
      .addBodyPart(new StringPart("device_timestamp", (System.currentTimeMillis / 1000).toString))
    def addCookies(cookies: List[Cookie], req: Req): Req = {
      if (cookies.isEmpty) req
      else addCookies(cookies.tail, req.addCookie(cookies.head))
    }
    val effectiveRequest = addCookies(cookies, request)
    send[String](effectiveRequest)
  }

  def configureImage(): Unit = {
  }

  def hashHmac(s: String, secret: String): String = {
    val sha256_HMAC = Mac.getInstance("HmacSHA256")
    sha256_HMAC.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"))
    sha256_HMAC.doFinal(s.getBytes("UTF-8")).map(char => f"$char%02x").mkString
  }

  def send[T](request: Req)(implicit r: Reads[T]): Future[Option[T]] = {
    Http(request).map { resp =>
      val response = resp.getResponseBody
      println(response)
      cookies = resp.getCookies.toList
      None
    }
  }

  Await.result(signIn(), Duration.Inf)
  postImage(new File("yukihirai.jpeg"))
}
