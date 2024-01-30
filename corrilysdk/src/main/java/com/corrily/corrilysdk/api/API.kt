package com.corrily.corrilysdk.api

import com.corrily.corrilysdk.dependencymanager.DependencyProtocol
import com.corrily.corrilysdk.misc.JSON
import com.corrily.corrilysdk.misc.Logger
import com.corrily.corrilysdk.models.IdentifyDto
import com.corrily.corrilysdk.models.IdentifyResponse
import com.corrily.corrilysdk.models.PaywallDto
import com.corrily.corrilysdk.models.PaywallResponse
import java.net.HttpURLConnection

class API(val factory: DependencyProtocol) {
  private suspend inline fun <reified Response> request(endpoint: Endpoint<Response>): Response {
    val request = endpoint.createURLRequest(factory = factory)

    request.getRequestProperty("X-Api-Key") ?: throw HttpError.NotAuthenticated()

    when (request.responseCode) {
      in 200 until 300 -> {
        return JSON.decodeFromString<Response>(request.readResponseAndDisconnect())
      }

      in 300 until 500 -> {
        val decoded = JSON.decodeFromString<ErrorResponse>(request.readResponseAndDisconnect())
        Logger.error(TAG, "request failed with code: ${request.responseCode} and message: ${decoded.errorMessage}")
        throw HttpError.ClientError(decoded.errorMessage)
      }

      else -> {
        Logger.error(TAG, "request unknown error")
        throw HttpError.Unknown()
      }
    }
  }

  suspend fun getPaywall(dto: PaywallDto): PaywallResponse {
    return try {
      request(Endpoint.paywall(dto))
    } catch (error: Throwable) {
      Logger.error(TAG, "Get paywall ${error.message}")
      throw error
    }
  }

  suspend fun identifyUser(dto: IdentifyDto): IdentifyResponse {
    Logger.info(TAG, "Identify User")
    return try {
      request(Endpoint.identify(dto))
    } catch (error: Throwable) {
      Logger.error(TAG, "Identify user ${error.message}")
      throw error
    }
  }

  private fun HttpURLConnection.readResponseAndDisconnect(): String {
    val text = this.inputStream.bufferedReader().use { it.readText() }
    this.disconnect()
    return text
  }


  companion object {
    private const val TAG = "API"
  }
}

