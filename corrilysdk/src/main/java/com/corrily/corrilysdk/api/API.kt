package com.corrily.corrilysdk.api

import com.corrily.corrilysdk.dependencymanager.DependencyProtocol
import com.corrily.corrilysdk.misc.JSON
import com.corrily.corrilysdk.models.IdentifyDto
import com.corrily.corrilysdk.models.IdentifyResponse
import com.corrily.corrilysdk.models.PaywallDto
import com.corrily.corrilysdk.models.PaywallResponse
import java.net.HttpURLConnection

class API(val factory: DependencyProtocol) {
  private suspend inline fun <reified Response> request(endpoint: Endpoint<Response>): Response {
    println("In side request")
    val request = endpoint.createURLRequest(factory = factory) ?:
      throw HttpError.InvalidRequest()

    request.getRequestProperty("X-Api-Key") ?:
      throw HttpError.NotAuthenticated()

    println("request ${request.responseCode}")
    when (request.responseCode) {
      in 200 until 300 -> {
        println("request 200..299")
        return JSON.decodeFromString<Response>(request.readResponseAndDisconnect())
      }
      in 300 until 500 -> {
        val decoded = JSON.decodeFromString<ErrorResponse>(request.readResponseAndDisconnect())
        println("request 300..499 $decoded")
        throw HttpError.ClientError(decoded.errorMessage)
      }
      else -> {
        println("request unknown")
        throw HttpError.Unknown()
      }
    }
  }

  suspend fun getPaywall(dto: PaywallDto): PaywallResponse {
    println("getPaywall $dto")
    return try {
      request(Endpoint.paywall(dto))
    } catch (error: Throwable) {
      println("getPaywall e ${error.message}")
      throw error
    }
  }
  suspend fun identifyUser(dto: IdentifyDto): IdentifyResponse {
    return try {
      request(Endpoint.identify(dto))
    } catch (error: Throwable) {
      throw error
    }
  }

  private fun HttpURLConnection.readResponseAndDisconnect(): String {
    val text = this.inputStream.bufferedReader().use { it.readText() }
    this.disconnect()
    return text
  }
}

