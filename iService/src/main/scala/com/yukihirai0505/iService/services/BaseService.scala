package com.yukihirai0505.iService.services

import com.typesafe.scalalogging.LazyLogging
import dispatch.{Http, Req}
import play.api.libs.json.{JsError, JsSuccess, Json, Reads}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Yuky on 2017/10/06.
  */
trait BaseService extends LazyLogging {

  def requestWebPage[T](req: Req)(implicit ec: ExecutionContext, r: Reads[T]): Future[T] = {
    Http(req).map { resp =>
      val pattern = """<script type="text/javascript">window._sharedData =([\s\S]*?);</script>""".r
      val response = resp.getResponseBody
      if (resp.getStatusCode != 200) throw new Exception(response.toString)
      pattern.findFirstMatchIn(response).fold(throw new RuntimeException(s"pattern error occur url: ${req.url}")) { m =>
        Json.parse(m.group(1)).validate[T] match {
          case JsError(e) => throw new RuntimeException(e.toString())
          case JsSuccess(value, _) => value
        }
      }
    }
  }

}
