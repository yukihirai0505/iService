package com.yukihirai0505.iPost

import java.io.File

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.iPost.constans.{ContentType, NaturalMethods}
import com.yukihirai0505.iPost.utils.ReqUtil
import dispatch.{Future, Req}

import scala.concurrent.ExecutionContext.Implicits.global

class iPostNatural(username: String, password: String) {

  /**
    * Post Photo in natural ways
    *
    * @param postImage
    * @param caption
    * @return
    */
  def postNaturalWays(postImage: File, caption: String): Future[List[Cookie]] = {
    top().flatMap { c1 =>
      login(c1).flatMap { c2 =>
        top(c2).flatMap { c3 =>
          uploadPhoto(postImage, c3).flatMap { uploadId =>
            createConfigure(uploadId, caption, c3)
          }
        }
      }
    }
  }

  /**
    * Access Top page and get cookie
    *
    * @return
    */
  def top(cookies: List[Cookie] = List.empty[Cookie]): Future[List[Cookie]] = {
    Thread.sleep(3000)
    val req = ReqUtil.getNaturalReq(NaturalMethods.TOP, cookies)
    ReqUtil.sendRequest(req)
  }

  /**
    * Login with cookie and user info
    *
    * @param cookies
    * @return
    */
  def login(cookies: List[Cookie]): Future[List[Cookie]] = {
    Thread.sleep(3000)
    val body = Map(
      "username" -> username,
      "password" -> password
    )
    val req = ReqUtil.getNaturalReq(NaturalMethods.ACCOUNTS_LOGIN_AJAX, cookies, isAjax = true)
      .addHeader("Content-Type", ContentType.APPLICATION_X_WWW_FORM_URL_ENCODED)
      .addHeader("Referer", "https://www.instagram.com/") << body
    ReqUtil.sendRequest(req)
  }

  /**
    * Upload a photo
    *
    * @param postImage
    * @param cookies
    * @return
    */
  def uploadPhoto(postImage: File, cookies: List[Cookie]): Future[String] = {
    Thread.sleep(3000)
    val uploadId = System.currentTimeMillis.toString
    val req: Req = ReqUtil.getNaturalReq(NaturalMethods.CREATE_UPLOAD_PHOTO, cookies, isAjax = true)
      .setMethod("POST")
      .addHeader("Referer", "https://www.instagram.com/create/crop/")
      .addBodyPart(new StringPart("upload_id", uploadId, ContentType.TEXT_PLAIN))
      .addBodyPart(new FilePart("photo", postImage, ContentType.IMAGE_JPEG)) // Browser allowed only jpg
      .addBodyPart(new StringPart("media_type", "1", ContentType.TEXT_PLAIN))
    ReqUtil.sendRequest(req).flatMap(_ => Future successful uploadId)
  }

  /**
    * Share a photo with caption
    *
    * @param uploadId
    * @param caption
    * @param cookies
    * @return
    */
  def createConfigure(uploadId: String, caption: String, cookies: List[Cookie]): Future[List[Cookie]] = {
    Thread.sleep(3000)
    val body = Map(
      "upload_id" -> uploadId,
      "caption" -> caption
    )
    val req = ReqUtil.getNaturalReq(NaturalMethods.CREATE_CONFIGURE, cookies, isAjax = true)
      .addHeader("Content-Type", ContentType.APPLICATION_X_WWW_FORM_URL_ENCODED)
      .addHeader("Referer", "https://www.instagram.com/create/details/") << body
    ReqUtil.sendRequest(req)
  }

}
