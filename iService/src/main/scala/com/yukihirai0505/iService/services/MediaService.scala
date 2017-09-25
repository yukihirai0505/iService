package com.yukihirai0505.iService.services

import java.io.File
import java.net.URLEncoder

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iService.constans.{ContentType, Methods}
import com.yukihirai0505.iService.responses._
import com.yukihirai0505.iService.utils.ReqUtil
import dispatch.{Http, Req}
import play.api.libs.json.{JsError, JsSuccess, Json}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Yuky on 2017/09/25.
  */
object MediaService {
  def getPosts(hashTag: String, cookies: List[Cookie])
              (implicit ec: ExecutionContext): Future[Tag] = {
    val hashTagUrl: String = s"${Methods.Natural.HASH_TAG_URL(URLEncoder.encode(hashTag, "UTF-8"))}"
    val req: Req = ReqUtil.getNaturalReq(hashTagUrl, cookies).setMethod("GET")
    Http(req).map { resp =>
      val pattern = """<script type="text/javascript">window._sharedData =([\s\S]*?);</script>""".r
      val response = resp.getResponseBody
      if (resp.getStatusCode != 200) throw new Exception(response.toString)
      pattern.findFirstMatchIn(response).fold(throw new RuntimeException("couldn't get user info")) { m =>
        Json.parse(m.group(1)).validate[MediaData] match {
          case JsError(e) =>
            val errorMessage = s"----Response: ${response.toString}\n\n----ErrorMessage: ${e.toString}"
            throw new RuntimeException(errorMessage)
          case JsSuccess(value, _) =>
            value.entryData.TagPage.head.tag
        }
      }
    }
  }

  def postNaturalWays(postImage: File, caption: String, cookies: List[Cookie])
                     (implicit ec: ExecutionContext): Future[Either[Throwable, Status]] = {
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

    def createConfigure(uploadId: String, caption: String, cookies: List[Cookie], sleepTime: Int = 5000): Future[Either[Throwable, Status]] = {
      Thread.sleep(sleepTime)
      val body = Map(
        "upload_id" -> uploadId,
        "caption" -> caption
      )
      val req = ReqUtil.getNaturalReq(Methods.Natural.CREATE_CONFIGURE, cookies, isAjax = true)
        .addHeader("Content-Type", ContentType.APPLICATION_X_WWW_FORM_URL_ENCODED)
        .addHeader("Referer", "https://www.instagram.com/create/details/") << body
      Request.sendRequestJson[CreateConfigure](req).flatMap {
        case Response(Some(v), _) => Future successful Right(Status(v.status, v.media.map(_.code)))
        case _ => Future successful Left(throw new RuntimeException("iPostNatural createConfigure failed"))
      }
    }

    uploadPhoto(postImage, cookies).flatMap {
      case Right(uploadId) => createConfigure(uploadId, caption, cookies)
      case Left(e) => Future successful Left(e)
    }
  }

}
