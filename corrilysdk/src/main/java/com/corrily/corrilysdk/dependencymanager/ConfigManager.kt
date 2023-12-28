package com.corrily.corrilysdk.dependencymanager

class ConfigManager {
  enum class Channel(val baseUrl: String) {
    Production("https://client.corrily.com"),
    Staging("https://staging.corrily.com/mainapi")
  }

  var channel: Channel = Channel.Production

  var apiKey: String = ""
    private set

  fun setApiKey(apiKey: String) {
    this.apiKey = apiKey
  }
}