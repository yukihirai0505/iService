package com.yukihirai0505.iPost

import java.io.File
import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import play.api.libs.json.{JsError, JsSuccess, Json, Reads}

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.iPost.constans.Methods
import com.yukihirai0505.iPost.responses.MediaUpload
import dispatch.{Future, Http, Req, url}

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global

class iPost(username: String, password: String) {

  var cookies: List[Cookie] = List.empty
  val uuid = java.util.UUID.randomUUID.toString
  val deviceId = s"android-$uuid"

  def login(): Future[Option[String]] = {
    val params = s"""{"username":"$username","password":"$password","guid":"$uuid","device_id":"$deviceId"}"""
    val body = s"ig_sig_key_version=4&signed_body=${hashHmac(params, "b4a23f5e39b5929e0666ac5de94c89d1618a2916")}.${URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\-", "%2D")}"
    send[String](createRequest(Methods.ACCOUNTS_LOGIN).setBody(body))
  }

  def mediaUpload(postImage: File): Future[Option[String]] = {
    val request: Req = createRequest(Methods.MEDIA_UPLOAD, isFormUrlEncoded = false)
      .addBodyPart(new FilePart("photo", postImage))
      .addBodyPart(new StringPart("device_timestamp", timestamp))
    val effectiveRequest = addCookies(cookies, request)

    sendMaji[MediaUpload](effectiveRequest).flatMap {
      case Some(v) => Future successful Some(v.mediaId)
      case _ => Future successful None
    }

  }

  def mediaConfigure(mediaId: String, caption: String): Future[Option[String]] = {
    val params = s"""{"guid":"$uuid","device_id":"$deviceId","device_timestamp":"$timestamp","media_id":"$mediaId","caption":"$caption","source_type":"5","filter_type":"0","extra":"{}"}"""
    val body = s"ig_sig_key_version=4&signed_body=${hashHmac(params, "b4a23f5e39b5929e0666ac5de94c89d1618a2916")}.${URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\-", "%2D")}"
    val request: Req = addCookies(cookies, createRequest(Methods.MEDIA_CONFIGURE).setBody(body))
    send[String](request)
  }

  def hashHmac(s: String, secret: String): String = {
    val sha256_HMAC = Mac.getInstance("HmacSHA256")
    sha256_HMAC.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"))
    sha256_HMAC.doFinal(s.getBytes("UTF-8")).map(char => f"$char%02x").mkString
  }

  def timestamp: String = {
    (System.currentTimeMillis / 1000).toString
  }

  def send[T](request: Req)(implicit r: Reads[T]): Future[Option[T]] = {
    Http(request).map { resp =>
      val response = resp.getResponseBody
      println(response)
      cookies = resp.getCookies.toList
      None
    }
  }

  def sendMaji[T](request: Req)(implicit r: Reads[T]): Future[Option[T]] = {
    Http(request).map { resp =>
      val response = resp.getResponseBody
      println(response)
      val csrftoken = resp.getCookies.filter(v => v.getName.equals("csrftoken")).head
      cookies :+ csrftoken
      Json.parse(response).validate[T] match {
        case JsError(e) => throw new Exception(e.toString())
        case JsSuccess(value, _) => value match {
          case None => None
          case _ => Some(value)
        }
      }
    }
  }

  def createRequest(requestUrl: String, isFormUrlEncoded: Boolean = true): Req = {
    val req = url(requestUrl)
      .setMethod(Verbs.POST.label)
      .addHeader("User-Agent", "Instagram 4.0.0 Android (10/3.3.3; 240; 480x320; samsung; GT-I9220; GT-I9220; smdkc210; en_US)")
    if (isFormUrlEncoded) req.setContentType("application/x-www-form-urlencoded", "UTF-8") else req
  }

  def addCookies(cookies: List[Cookie], req: Req): Req = {
    if (cookies.isEmpty) req
    else addCookies(cookies.tail, req.addCookie(cookies.head))
  }

}
