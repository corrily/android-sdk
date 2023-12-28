package com.corrily.corrilysdk.api

import com.corrily.corrilysdk.dependencymanager.DependencyProtocol
import com.corrily.corrilysdk.misc.JSON
import com.corrily.corrilysdk.models.IdentifyDto
import com.corrily.corrilysdk.models.IdentifyResponse
import com.corrily.corrilysdk.models.PaywallDto
import com.corrily.corrilysdk.models.PaywallResponse
import kotlinx.coroutines.coroutineScope
import java.net.HttpURLConnection
import java.net.URL

data class URLQueryItem(val name: String, val value: String)

class Endpoint<Response>(
  private val path: String,
  private val method: HttpMethod = HttpMethod.Get,
  private val queryItems: List<URLQueryItem>? = null,
  private val body: ByteArray? = null
) {

  enum class HttpMethod(val method: String) {
    Get("GET"),
    Post("POST")
  }
  suspend fun createURLRequest(factory: DependencyProtocol): HttpURLConnection? = coroutineScope {
    println("inside create request")
    val queries = queryItems?.joinToString("&") { "${it.name}=${it.value}"} ?: ""
    val url = URL("${factory.config.channel.baseUrl}${path}?${queries}")
    println("request url ${factory.config.channel.baseUrl}${path}?${queries}")
    val connection = url.openConnection() as HttpURLConnection

    val headers: Map<String, String> = mapOf(
      "X-Api-Key" to factory.config.apiKey,
      "Content-Type" to "application/json"
    )

    headers.map {
      connection.setRequestProperty(it.key, it.value)
    }

    connection.doOutput = method.method == HttpMethod.Post.method
    if (body != null) {
      connection.doInput = true
      val outputStream = connection.outputStream
      outputStream.write(body)
      outputStream.close()
    }

    connection.requestMethod = method.method

    return@coroutineScope connection
  }

  companion object {
    fun paywall(dto: PaywallDto): Endpoint<PaywallResponse> {

      val body = JSON.encodeToString<PaywallDto>(dto)

      println("request body $body")

      return Endpoint(
        path = "/v1/paywall",
        method = HttpMethod.Post,
        body = body.toByteArray()
      )
    }

    fun identify(dto: IdentifyDto): Endpoint<IdentifyResponse> {
      val body = JSON.encodeToString<IdentifyDto>(dto)
      return Endpoint(
        path = "/v1/identify",
        method = HttpMethod.Post,
        body = body.toByteArray()
      )
    }
  }
}