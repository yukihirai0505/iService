package com.yukihirai0505.iPost

import java.io.File

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iPost.constans.{ContentType, Methods}
import com.yukihirai0505.iPost.responses.{CreateConfigure, UploadPhoto}
import com.yukihirai0505.iPost.utils.ReqUtil
import dispatch.{Future, Req}

import scala.concurrent.ExecutionContext.Implicits.global

class iPostNatural(username: String, password: String) {

  case class Result(status: Boolean, code: String)

  /**
    * Post Photo in natural ways
    *
    * @param postImage
    * @param caption
    * @return
    */
  def postNaturalWays(postImage: File, caption: String): Future[Either[Throwable, Result]] = {
    top().flatMap { c1 =>
      login(c1).flatMap { c2 =>
        top(c2).flatMap { c3 =>
          uploadPhoto(postImage, c3).flatMap {
            case Right(uploadId) => createConfigure(uploadId, caption, c3)
            case Left(e) => Future successful Left(e)
          }
        }
      }
    }.recover { case e: Exception => Left(e) }
  }

  /**
    * Access Top page and get cookie
    *
    * @return
    */
  def top(cookies: List[Cookie] = List.empty[Cookie], sleepTime: Int = 0): Future[List[Cookie]] = {
    Thread.sleep(sleepTime)
    val req = ReqUtil.getNaturalReq(Methods.Natural.TOP, cookies)
    ReqUtil.sendRequest(req)
  }

  /**
    * Login with cookie and user info
    *
    * @param cookies
    * @return
    */
  def login(cookies: List[Cookie], sleepTime: Int = 3000): Future[List[Cookie]] = {
    Thread.sleep(sleepTime)
    val body = Map(
      "username" -> username,
      "password" -> password
    )
    val req = ReqUtil.getNaturalReq(Methods.Natural.ACCOUNTS_LOGIN_AJAX, cookies, isAjax = true)
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
  def uploadPhoto(postImage: File, cookies: List[Cookie], sleepTime: Int = 3000): Future[Either[Throwable, String]] = {
    Thread.sleep(sleepTime)
    val uploadId = System.currentTimeMillis.toString
    val req: Req = ReqUtil.getNaturalReq(Methods.Natural.CREATE_UPLOAD_PHOTO, cookies, isAjax = true)
      .setMethod("POST")
      .addHeader("Referer", "https://www.instagram.com/create/crop/")
      .addBodyPart(new StringPart("upload_id", uploadId, ContentType.TEXT_PLAIN))
      .addBodyPart(new FilePart("photo", postImage, ContentType.IMAGE_JPEG)) // Browser allowed only jpg
      .addBodyPart(new StringPart("media_type", "1", ContentType.TEXT_PLAIN))
    Request.sendRequestJson[UploadPhoto](req).flatMap {
      case Response(Some(v), _) => Future successful Right(v.uploadId)
      case _ => Future successful Left(throw new RuntimeException("iPostNatural uploadPhoto failed"))
    }
  }

  /**
    * Share a photo with caption
    *
    * @param uploadId
    * @param caption
    * @param cookies
    * @return
    */
  def createConfigure(uploadId: String, caption: String, cookies: List[Cookie], sleepTime: Int = 5000): Future[Either[Throwable, Result]] = {
    Thread.sleep(sleepTime)
    val body = Map(
      "upload_id" -> uploadId,
      "caption" -> caption
    )
    val req = ReqUtil.getNaturalReq(Methods.Natural.CREATE_CONFIGURE, cookies, isAjax = true)
      .addHeader("Content-Type", ContentType.APPLICATION_X_WWW_FORM_URL_ENCODED)
      .addHeader("Referer", "https://www.instagram.com/create/details/") << body
    Request.sendRequestJson[CreateConfigure](req).flatMap {
      case Response(Some(v), _) => Future successful Right(Result(v.status == "ok", v.media.fold("")(v => v.code)))
      case _ => Future successful Left(throw new RuntimeException("iPostNatural createConfigure failed"))
    }
  }

}
