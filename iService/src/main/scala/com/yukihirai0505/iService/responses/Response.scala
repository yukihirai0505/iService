package com.yukihirai0505.iService.responses

case class Response[T](data: Option[T], response: com.ning.http.client.Response)
