package com.yukihirai0505.iPost

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.iPost.constans.NaturalMethods
import com.yukihirai0505.iPost.utils.{HashUtil, ReqUtil}
import dispatch.Future

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


  /***
    * Access Top page and get cookie
    * @return
    */
  def top(): Future[List[Cookie]] = {
    val req = ReqUtil.getNaturalReq(NaturalMethods.TOP, Verbs.GET)
    ReqUtil.sendRequest(req)
  }

  /***
    * Login with cookie and user info
    * @param cookies
    * @return
    */
  def login(cookies: List[Cookie]): Future[List[Cookie]] = {
    val body = Map(
      "username" -> username,
      "password" -> password
    )
    val req = ReqUtil.getNaturalReq(NaturalMethods.ACCOUNTS_LOGIN_AJAX, Verbs.POST, cookies) << body
    ReqUtil.sendRequest(req)
  }

  /***
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

    */

}
