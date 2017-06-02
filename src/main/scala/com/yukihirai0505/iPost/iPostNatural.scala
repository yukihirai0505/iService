package com.yukihirai0505.iPost

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.iPost.constans.NaturalMethods
import com.yukihirai0505.iPost.utils.ReqUtil
import dispatch.Future

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
    val req = ReqUtil.getNaturalReq(NaturalMethods.ACCOUNTS_LOGIN_AJAX, cookies, isAjax = true) << body
    ReqUtil.sendRequest(req)
  }

  /***
  def mediaUpload(postImage: File, cookies: List[Cookie]): Future[Option[String]] = {
    val request: Req = ReqUtil.getReq(APIMethods.MEDIA_UPLOAD, isFormUrlEncoded = false, cookies)
      .addBodyPart(new FilePart("photo", postImage))
      .addBodyPart(new StringPart("device_timestamp", timestamp))
    Request.send[MediaUpload](request).flatMap {
      case Some(v) => Future successful Some(v.mediaId)
      case _ => Future successful None
    }

  }

  def mediaConfigure(mediaId: String, caption: String, cookies: List[Cookie]): Future[List[Cookie]] = {
    val json = Json.prettyPrint(Json.toJson(MediaConfigure(uuid, deviceId, timestamp, mediaId, caption)))
    val request: Req = ReqUtil.getReq(APIMethods.MEDIA_CONFIGURE, cookies = cookies).setBody(createSingedBody(json))
    sendRequest(request)
  }
    */

}
