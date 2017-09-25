package com.yukihirai0505.iMedia

import com.ning.http.client.cookie.Cookie
import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.iMedia.responses.{MediaData, Tag}
import com.yukihirai0505.iService.common.InstagramUser
import com.yukihirai0505.iService.common.constans.Methods
import com.yukihirai0505.iService.common.utils.ReqUtil
import dispatch.{Future, Http, Req}
import play.api.libs.json.{JsError, JsSuccess, Json}

import scala.concurrent.ExecutionContext.Implicits.global

class iMedia(username: String, password: String) extends InstagramUser(username, password) with LazyLogging {

  def getSearchHashTagResult(hashTag: String): Future[Either[Throwable, Tag]] = {
    val hashTagUrl: String = s"${Methods.Natural.HASH_TAG_URL(hashTag)}"

    def getPosts(cookies: List[Cookie]): Future[Tag] = {
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

    login().flatMap { cookies: List[Cookie] =>
      getPosts(cookies).flatMap { tag => Future successful Right(tag) }
    }.recover { case e: Exception => Left(e) }
  }
}