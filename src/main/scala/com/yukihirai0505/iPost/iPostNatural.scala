package com.yukihirai0505.iPost

import java.io.File

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.iPost.constans.Constants.CONTENT_TYPE
import com.yukihirai0505.iPost.constans.NaturalMethods
import com.yukihirai0505.iPost.utils.ReqUtil
import dispatch.{Future, Req}

class iPostNatural(username: String, password: String) {

  /***
    * Access Top page and get cookie
    * @return
    */
  def top(cookies: List[Cookie] = List.empty[Cookie]): Future[List[Cookie]] = {
    val req = ReqUtil.getNaturalReq(NaturalMethods.TOP, cookies)
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
    val req = ReqUtil.getNaturalReq(NaturalMethods.ACCOUNTS_LOGIN_AJAX, cookies, isAjax = true)
      .addHeader("Content-Type", CONTENT_TYPE)
      .addHeader("Referer", "https://www.instagram.com/") << body
    ReqUtil.sendRequest(req)
  }

  def uploadPhoto(postImage: File, cookies: List[Cookie]): (Future[List[Cookie]], String) = {
    val uploadId = System.currentTimeMillis.toString
    val req: Req = ReqUtil.getNaturalReq(NaturalMethods.CREATE_UPLOAD_PHOTO, cookies, isAjax = true)
      .setMethod("POST")
      .addHeader("Referer", "https://www.instagram.com/create/crop/")
      .addBodyPart(new StringPart("upload_id", uploadId, "text/plain"))
      .addBodyPart(new FilePart("photo", postImage, "image/jpeg")) // contentTypeがデフォルトあかんのでファイルみてcontentType判断しないといけないね
      .addBodyPart(new StringPart("media_type", "1", "text/plain"))
    (ReqUtil.sendRequest(req), uploadId)
  }

  /***
  def mediaConfigure(mediaId: String, caption: String, cookies: List[Cookie]): Future[List[Cookie]] = {
    val json = Json.prettyPrint(Json.toJson(MediaConfigure(uuid, deviceId, timestamp, mediaId, caption)))
    val request: Req = ReqUtil.getReq(APIMethods.MEDIA_CONFIGURE, cookies = cookies).setBody(createSingedBody(json))
    sendRequest(request)
  }
    */

}
