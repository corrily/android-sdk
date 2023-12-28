package com.corrily.corrilysdk.models

import kotlinx.serialization.Serializable

@Serializable
data class IdentifyResponse(
  val status: String
)

@Serializable
data class IdentifyDto(
  val userId: String?,
  val ip: String?,
  val country: String?
)