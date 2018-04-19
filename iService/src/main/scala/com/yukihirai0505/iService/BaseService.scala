package com.yukihirai0505.iService

import java.util.concurrent.Executors

import com.ning.http.client.cookie.Cookie
import com.ning.http.client.{AsyncHttpClient, AsyncHttpClientConfig}
import com.typesafe.scalalogging.LazyLogging
import com.yukihirai0505.iService.constans.Constants.IOS_USER_AGENT
import com.yukihirai0505.iService.responses.Response
import com.yukihirai0505.iService.utils.NumberUtil
import dispatch.{Http, Req, Res, url}
import play.api.libs.json.{JsError, JsSuccess, Json, Reads}

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

trait BaseService extends LazyLogging {

  private var cookies: List[Cookie] = List.empty

  private val executorService = Executors.newFixedThreadPool(10)
  private val asyncHttpClient = new AsyncHttpClient(
    new AsyncHttpClientConfig.Builder()
      .setExecutorService(executorService)
      .setIOThreadMultiplier(1)
      .setAllowPoolingConnections(true)
      .setAllowPoolingSslConnections(true)
      .setConnectTimeout(3000)
      .setRequestTimeout(10000)
      .setCompressionEnforced(true)
      .setFollowRedirect(true).build)
  private val httpClient = new Http(asyncHttpClient)

  def getNaturalReq(requestUrl: String, isAjax: Boolean = false)(implicit ec: ExecutionContext): Req = {
    val baseReq = addCookies(url(requestUrl))
      .addHeader("User-Agent", IOS_USER_AGENT)
      .addHeader("Host", "www.instagram.com")
      .addHeader("Accept", "*/*")
      .addHeader("Accept-Encoding", "gzip, deflate, br")
      .addHeader("Accept-Language", "ja,en-US;q=0.8,en;q=0.6")
    val csrfToken = cookies.find(v => v.getName.equals("csrftoken")).map(_.getValue).getOrElse("")
    if (isAjax)
      baseReq
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("X-Instagram-AJAX", "1")
        .addHeader("X-CSRFToken", csrfToken)
    else baseReq
  }

  // Request

  def sendRequest(request: Req)(implicit ec: ExecutionContext): Future[String] = {
    Thread.sleep(NumberUtil.getRandomInt())
    Http(request).map { res =>
      getHttpResponse(res)
    }
  }

  def sendRequestJson[T](request: Req, charset: String = "UTF-8")(implicit ec: ExecutionContext, r: Reads[T]): Future[Response[T]] = {
    Thread.sleep(NumberUtil.getRandomInt())
    httpClient(request).map { resp =>
      val response = getHttpResponse(resp)
      try {
        if (resp.getStatusCode == 500) throw new Exception(response)
        Json.parse(response).validate[T] match {
          case JsError(e) =>
            val errorMessage = s"----ErrorMessage: ${e.toString}\n----url: ${request.url}\n----Response: $response\n"
            throw new Exception(errorMessage)
          case JsSuccess(value, _) => value match {
            case None => Response[T](None, resp)
            case _ => Response[T](Some(value), resp)
          }
        }
      } catch {
        case e: Exception =>
          val errorMessage = s"----ErrorMessage: ${e.getMessage}\n----url: ${request.url}\n----Response: $response\n"
          throw new Exception(errorMessage)
      }
    }
  }

  def requestWebPage[T](req: Req)(implicit ec: ExecutionContext, r: Reads[T]): Future[Either[Throwable, T]] = {
    Thread.sleep(NumberUtil.getRandomInt())
    Http(req).map { resp =>
      val pattern = """<script type="text/javascript">window._sharedData =([\s\S]*?);</script>""".r
      val response = getHttpResponse(resp)
      resp.getStatusCode match {
        case statusCode if statusCode == 404 =>
          Left(new Exception(constans.Constants.NOT_FOUND_ERROR_MESSAGE))
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

  private def getHttpResponse(res: Res): String = {
    val _cookies = res.getCookies.toList
    if (_cookies.nonEmpty) {
      cookies = res.getCookies.toList
    }
    res.getResponseBody
  }

  private def addCookies(req: Req, _cookies: List[Cookie] = cookies): Req = {
    if (_cookies.isEmpty) req
    else addCookies(req.addCookie(_cookies.head), _cookies.tail)
  }

}
