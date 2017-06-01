package com.yukihirai0505.iPost

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.iPost.constans.Constants.IOS_USER_AGENT
import com.yukihirai0505.iPost.constans.NaturalMethods
import com.yukihirai0505.iPost.utils.{HashUtil, ReqUtil}
import dispatch.{Future, url}

class iPostNatural(username: String, password: String) {

  private val uuid: String = HashUtil.createUUID
  private val deviceId = s"android-$uuid"

  /***
    * - ログインページへアクセス
    *     - https://www.instagram.com/
    * - ログイン
    *     - https://www.instagram.com/accounts/login/ajax
    * - 写真アップ
    *     - https://www.instagram.com/create/upload/photo
    * - シェア
    *     - https://www.instagram.com/create/configure
    * @return
    */


  // TOP Page = Login Page
  def top(): Future[List[Cookie]] = {
    val req = url(NaturalMethods.TOP)
      .setMethod(Verbs.GET.label)
      .addHeader("User-Agent", IOS_USER_AGENT)
    ReqUtil.sendRequest(req)
  }

  /***
  // TODO クッキーつかってusername, password入力してログイン
  def login(): Future[List[Cookie]] = {
    val json = Json.prettyPrint(Json.toJson(Login(username, password, uuid, deviceId)))
    sendRequest(ReqUtil.getReq(APIMethods.ACCOUNTS_LOGIN).setBody(createSingedBody(json)))
  }

  // TODO クッキーつかってファイルアップ
  def mediaUpload(postImage: File, cookies: List[Cookie]): Future[Option[String]] = {
    val request: Req = ReqUtil.getReq(APIMethods.MEDIA_UPLOAD, isFormUrlEncoded = false, cookies)
      .addBodyPart(new FilePart("photo", postImage))
      .addBodyPart(new StringPart("device_timestamp", timestamp))
    Request.send[MediaUpload](request).flatMap {
      case Some(v) => Future successful Some(v.mediaId)
      case _ => Future successful None
    }

  }

  // TODO クッキーつかってシェア
  def mediaConfigure(mediaId: String, caption: String, cookies: List[Cookie]): Future[List[Cookie]] = {
    val json = Json.prettyPrint(Json.toJson(MediaConfigure(uuid, deviceId, timestamp, mediaId, caption)))
    val request: Req = ReqUtil.getReq(APIMethods.MEDIA_CONFIGURE, cookies = cookies).setBody(createSingedBody(json))
    sendRequest(request)
  }

  // TODO
  def sendRequest(request: Req): Future[List[Cookie]] = {
    Http(request).map { resp =>
      resp.getCookies.toList
    }
  }

  def createSingedBody(json: String): String = {
    s"ig_sig_key_version=4&signed_body=${HashUtil.hashHmac(json, HASH_HMAC_KEY)}.${URLEncoder.encode(json, UTF8).replaceAll("\\+", "%20").replaceAll("\\-", "%2D")}"
  }
    */

}
