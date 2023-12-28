package com.corrily.corrilysdk.api

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
  val success: Boolean = false,
  val errorMessage: String
)

sealed class HttpError(message: String): Throwable(message) {
  class InvalidRequest: HttpError("Request invalid")
  class NotAuthenticated: HttpError("Unauthorized")
  class Unknown: HttpError("Unknown")

  class ClientError(serverError: String): HttpError(serverError)
}