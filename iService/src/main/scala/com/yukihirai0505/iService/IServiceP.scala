package com.yukihirai0505.iService

import java.io.File
import java.net.URLEncoder

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iService.constans.Constants.{HASH_HMAC_KEY, UTF8}
import com.yukihirai0505.iService.constans.Methods
import com.yukihirai0505.iService.utils.{DateUtil, HashUtil, NumberUtil, ReqUtil}
import com.yukihirai0505.iService.responses.{Login, MediaConfigure, MediaUpload}
import dispatch.Req
import play.api.libs.json.Json

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Yuky on 2017/09/25.
  */
class IServiceP(username: String, password: String) {
  private val uuid: String = HashUtil.createUUID
  private val deviceId = s"android-$uuid"

  def post(postImage: File, caption: String): Future[List[Cookie]] = {
    def login(): Future[List[Cookie]] = {
      Thread.sleep(NumberUtil.getRandomInt())
      val json = Json.prettyPrint(Json.toJson(Login(username, password, uuid, deviceId)))
      ReqUtil.sendRequest(ReqUtil.getApiReq(Methods.Private.ACCOUNTS_LOGIN).setBody(createSingedBody(json)))
    }

    def mediaUpload(postImage: File, cookies: List[Cookie]): Future[Option[String]] = {
      Thread.sleep(NumberUtil.getRandomInt())
      val request: Req = ReqUtil.getApiReq(Methods.Private.MEDIA_UPLOAD, isFormUrlEncoded = false, cookies)
        .addBodyPart(new FilePart("photo", postImage))
        .addBodyPart(new StringPart("device_timestamp", DateUtil.timestamp))
      Request.sendRequestJson[MediaUpload](request).flatMap {
        case Response(Some(v), _) => Future successful Some(v.mediaId)
        case _ => Future successful None
      }

    }

    def mediaConfigure(mediaId: String, caption: String, cookies: List[Cookie]): Future[List[Cookie]] = {
      Thread.sleep(NumberUtil.getRandomInt())
      val json = Json.prettyPrint(Json.toJson(MediaConfigure(uuid, deviceId, DateUtil.timestamp, mediaId, caption)))
      val request: Req = ReqUtil.getApiReq(Methods.Private.MEDIA_CONFIGURE, cookies = cookies).setBody(createSingedBody(json))
      ReqUtil.sendRequest(request)
    }

    def createSingedBody(json: String): String = {
      s"ig_sig_key_version=4&signed_body=${HashUtil.hashHmac(json, HASH_HMAC_KEY)}.${URLEncoder.encode(json, UTF8).replaceAll("\\+", "%20").replaceAll("\\-", "%2D")}"
    }

    login().flatMap { c1 =>
      mediaUpload(postImage, c1).flatMap { mediaId =>
        mediaConfigure(mediaId.getOrElse(""), caption, c1)
      }
    }
  }
}
