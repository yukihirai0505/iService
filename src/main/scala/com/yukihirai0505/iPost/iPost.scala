package com.yukihirai0505.iPost

import java.io.File
import java.net.URLEncoder

import play.api.libs.json.Json

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.iPost.constans.Constants.hashHmacKey
import com.yukihirai0505.iPost.constans.Methods
import com.yukihirai0505.iPost.models.{Login, MediaConfigure}
import com.yukihirai0505.iPost.responses.MediaUpload
import com.yukihirai0505.iPost.utils.{HashUtil, ReqUtil}
import dispatch.{Future, Http, Req}

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global

class iPost(username: String, password: String) {

  private val uuid: String = HashUtil.createUUID
  private val deviceId = s"android-$uuid"

  def login(): Future[List[Cookie]] = {
    val json = Json.prettyPrint(Json.toJson(Login(username, password, uuid, deviceId)))
    sendRequest(ReqUtil.getReq(Methods.ACCOUNTS_LOGIN).setBody(createSingedBody(json)))
  }

  def mediaUpload(postImage: File, cookies: List[Cookie]): Future[Option[String]] = {
    val request: Req = ReqUtil.getReq(Methods.MEDIA_UPLOAD, isFormUrlEncoded = false)
      .addBodyPart(new FilePart("photo", postImage))
      .addBodyPart(new StringPart("device_timestamp", timestamp))
    val effectiveRequest = ReqUtil.addCookies(cookies, request)

    Request.send[MediaUpload](effectiveRequest).flatMap {
      case Some(v) => Future successful Some(v.mediaId)
      case _ => Future successful None
    }

  }

  def mediaConfigure(mediaId: String, caption: String, cookies: List[Cookie]): Future[List[Cookie]] = {
    val json = Json.prettyPrint(Json.toJson(MediaConfigure(uuid, deviceId, timestamp, mediaId, caption)))
    val request: Req = ReqUtil.addCookies(cookies, ReqUtil.getReq(Methods.MEDIA_CONFIGURE).setBody(createSingedBody(json)))
    sendRequest(request)
  }

  def sendRequest(request: Req): Future[List[Cookie]] = {
    Http(request).map { resp =>
      resp.getCookies.toList
    }
  }

  def createSingedBody(json: String): String = {
    s"ig_sig_key_version=4&signed_body=${HashUtil.hashHmac(json, hashHmacKey)}.${URLEncoder.encode(json, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\-", "%2D")}"
  }

  def timestamp: String = {
    (System.currentTimeMillis / 1000).toString
  }

}
