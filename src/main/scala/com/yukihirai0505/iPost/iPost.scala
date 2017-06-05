package com.yukihirai0505.iPost

import java.io.File
import java.net.URLEncoder

import play.api.libs.json.Json

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.iPost.constans.APIMethods
import com.yukihirai0505.iPost.constans.Constants.{HASH_HMAC_KEY, UTF8}
import com.yukihirai0505.iPost.models.{Login, MediaConfigure}
import com.yukihirai0505.iPost.responses.MediaUpload
import com.yukihirai0505.iPost.utils.{DateUtil, HashUtil, ReqUtil}
import dispatch.{Future, Req}

import scala.concurrent.ExecutionContext.Implicits.global

class iPost(username: String, password: String) {

  private val uuid: String = HashUtil.createUUID
  private val deviceId = s"android-$uuid"

  def post(postImage: File, caption: String): Future[List[Cookie]] = {
    login().flatMap { c1 =>
      mediaUpload(postImage, c1).flatMap { mediaId =>
        mediaConfigure(mediaId.getOrElse(""), caption, c1)
      }
    }
  }

  def login(): Future[List[Cookie]] = {
    val json = Json.prettyPrint(Json.toJson(Login(username, password, uuid, deviceId)))
    ReqUtil.sendRequest(ReqUtil.getApiReq(APIMethods.ACCOUNTS_LOGIN).setBody(createSingedBody(json)))
  }

  def mediaUpload(postImage: File, cookies: List[Cookie]): Future[Option[String]] = {
    Thread.sleep(3000)
    val request: Req = ReqUtil.getApiReq(APIMethods.MEDIA_UPLOAD, isFormUrlEncoded = false, cookies)
      .addBodyPart(new FilePart("photo", postImage))
      .addBodyPart(new StringPart("device_timestamp", DateUtil.timestamp))
    Request.send[MediaUpload](request).flatMap {
      case Some(v) => Future successful Some(v.mediaId)
      case _ => Future successful None
    }

  }

  def mediaConfigure(mediaId: String, caption: String, cookies: List[Cookie]): Future[List[Cookie]] = {
    Thread.sleep(3000)
    val json = Json.prettyPrint(Json.toJson(MediaConfigure(uuid, deviceId, DateUtil.timestamp, mediaId, caption)))
    val request: Req = ReqUtil.getApiReq(APIMethods.MEDIA_CONFIGURE, cookies = cookies).setBody(createSingedBody(json))
    ReqUtil.sendRequest(request)
  }

  def createSingedBody(json: String): String = {
    s"ig_sig_key_version=4&signed_body=${HashUtil.hashHmac(json, HASH_HMAC_KEY)}.${URLEncoder.encode(json, UTF8).replaceAll("\\+", "%20").replaceAll("\\-", "%2D")}"
  }

}
