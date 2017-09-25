package com.yukihirai0505.iService.services

import com.ning.http.client.cookie.Cookie
import com.yukihirai0505.iService.common.constans.Methods
import com.yukihirai0505.iService.common.utils.ReqUtil
import com.yukihirai0505.iService.responses.{MediaData, Tag}
import dispatch.{Future, Http, Req}
import play.api.libs.json.{JsError, JsSuccess, Json}

import scala.concurrent.ExecutionContext

/**
  * Created by Yuky on 2017/09/25.
  */
object MediaService {
  def getPosts(hashTag: String, cookies: List[Cookie])
              (implicit ec: ExecutionContext): Future[Tag] = {
    val hashTagUrl: String = s"${Methods.Natural.HASH_TAG_URL(hashTag)}"
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
}
