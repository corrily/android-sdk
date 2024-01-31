package com.corrily.corrilysdk.misc


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.serializer

object JSON {
  @OptIn(ExperimentalSerializationApi::class)
  val json = Json {
    ignoreUnknownKeys = true
    namingStrategy = JsonNamingStrategy.SnakeCase
  }

  inline fun <reified T> encodeToString(value: T): String {
    return json.encodeToString(serializer(), value)
  }

  inline fun <reified T> decodeFromString(value: String): T {
    return json.decodeFromString(serializer(), value)
  }
}