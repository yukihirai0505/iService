package com.yukihirai0505.iService.services

import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.iService.constans.Constants
import dispatch.{Http, Req}
import play.api.libs.json.{JsError, JsSuccess, Json, Reads}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Yuky on 2017/10/06.
  */
trait BaseService extends LazyLogging {

  def requestWebPage[T](req: Req)(implicit ec: ExecutionContext, r: Reads[T]): Future[Either[Throwable, T]] = {
    Http(req).map { resp =>
      val pattern = """<script type="text/javascript">window._sharedData =([\s\S]*?);</script>""".r
      val response = resp.getResponseBody
      resp.getStatusCode match {
        case statusCode if statusCode == 404 =>
          Left(new Exception(Constants.NOT_FOUND_ERROR_MESSAGE))
        case statusCode if statusCode != 200 =>
          Left(new Exception(s"statusCode: ${resp.getStatusCode}\nresponse: ${response.toString}"))
        case _ => pattern.findFirstMatchIn(response).fold(throw new Exception(s"pattern error occur url: ${req.url}")) { m =>
          Json.parse(m.group(1)).validate[T] match {
            case JsError(e) => Left(new Exception(e.toString()))
            case JsSuccess(value, _) => Right(value)
          }
        }
      }
    }
  }

}
