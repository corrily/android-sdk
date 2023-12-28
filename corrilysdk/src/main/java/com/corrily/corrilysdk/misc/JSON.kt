package com.corrily.corrilysdk.misc


import kotlinx.serialization.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

object JSON {
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