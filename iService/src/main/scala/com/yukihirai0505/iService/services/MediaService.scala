package com.yukihirai0505.iService.services

import java.io.File
import java.net.URLEncoder

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.iService.constans.{Constants, ContentType, Methods}
import com.yukihirai0505.iService.responses._
import com.yukihirai0505.iService.utils.{NumberUtil, ReqUtil}
import dispatch.Req

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Yuky on 2017/09/25.
  */
object MediaService extends BaseService {

  def getPostInfo(shortcode: String, cookies: List[Cookie] = List.empty)
                 (implicit ec: ExecutionContext): Future[Either[Throwable, PostPageGraphql]] = {
    val baseUrl = Methods.Natural.MEDIA_URL(shortcode)
    val req: Req = ReqUtil.getNaturalReq(baseUrl, cookies)
    requestWebPage[MediaData](req).flatMap {
      case Right(v) =>
        v.entryData.PostPage.headOption match {
          case Some(p) => Future successful Right(p.graphql)
          case None => Future successful Left(throw new RuntimeException("no post data"))
        }
      case Left(e) => if (e.getMessage.equals(Constants.NOT_FOUND_ERROR_MESSAGE)) {
        Future successful Left(throw new Exception(s"shortcode: $shortcode is cannot view"))
      } else Future successful Left(throw new Exception(e.getMessage))
    }
  }

  def getPosts(hashTag: String, cookies: List[Cookie] = List.empty)
              (implicit ec: ExecutionContext): Future[Either[Throwable, Tag]] = {
    val hashTagUrl: String = s"${Methods.Natural.HASH_TAG_URL(URLEncoder.encode(hashTag, "UTF-8"))}"
    val req: Req = ReqUtil.getNaturalReq(hashTagUrl, cookies).setMethod("GET")
    requestWebPage[HashTagData](req).flatMap {
      case Right(v) => Future successful Right(v.entryData.TagPage.head.tag)
      case Left(e) => if (e.getMessage.equals(Constants.NOT_FOUND_ERROR_MESSAGE)) {
        Future successful Left(throw new Exception(s"$hashTag is baned by instagram"))
      } else Future successful Left(throw new Exception(e.getMessage))
    }
  }

  def getPostsPaging(tagName: String, size: Int = 9, afterCode: String = "")
                    (implicit ec: ExecutionContext): Future[Either[Throwable, MediaQuery]] = {
    val pagingUrl: String = s"${Methods.Graphql.HASH_TAG_QUERY(tagName, size, afterCode)}"
    logger.info(s"getPostsPaging pagingUrl: $pagingUrl")
    val req: Req = ReqUtil.getNaturalReq(pagingUrl)
    Request.sendRequestJson[MediaQuery](req).flatMap {
      case Response(Some(v), _) => Future successful Right(v)
      case _ => Future successful Left(throw new RuntimeException("getPostsPaging failed"))
    }
  }

  def postNaturalWays(postImage: File, caption: String, cookies: List[Cookie])
                     (implicit ec: ExecutionContext): Future[Either[Throwable, Status]] = {
    def uploadPhoto(postImage: File, cookies: List[Cookie]): Future[Either[Throwable, String]] = {
      Thread.sleep(NumberUtil.getRandomInt())
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

    def createConfigure(uploadId: String, caption: String, cookies: List[Cookie]): Future[Either[Throwable, Status]] = {
      Thread.sleep(NumberUtil.getRandomInt())
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

  def deletePhoto(mediaId: String, shortcode: String, cookies: List[Cookie])
                 (implicit ec: ExecutionContext): Future[Either[Throwable, Status]] = {
    Thread.sleep(NumberUtil.getRandomInt())
    val req: Req = ReqUtil.getNaturalReq(Methods.Natural.CREATE_DELETE_PHOTO(mediaId), cookies, isAjax = true)
      .setMethod("POST")
      .addHeader("Referer", s"https://www.instagram.com/p/$shortcode/")
    Request.sendRequestJson[DeletePhoto](req).flatMap {
      case Response(Some(v), _) => Future successful Right(Status(v.status, None))
      case _ => Future successful Left(throw new RuntimeException("deletePhoto createConfigure failed"))
    }
  }
}
