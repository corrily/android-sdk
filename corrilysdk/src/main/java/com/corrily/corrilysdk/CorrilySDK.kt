package com.corrily.corrilysdk

import android.content.Context
import android.util.Log
import com.corrily.corrilysdk.dependencymanager.DependencyManager
import com.corrily.corrilysdk.models.PaywallDto
import com.corrily.corrilysdk.models.PaywallResponse

object CorrilySDK {
  private var initialized = false

  lateinit var dependencies: DependencyManager

  fun start(context: Context, apiKey: String) {
    if (initialized) {
      Log.d(
        "CorrilySDK",
        "Corrily.start called multiple times. Please make sure you only call this once on app launch."
      )
      return
    }
    dependencies = DependencyManager(context)
    dependencies.config.setApiKey(apiKey)
    initialized = true
  }

  suspend fun test(): PaywallResponse {
    val dto = PaywallDto(country = "US", ip = null, userId = null, paywallId = null)
    return dependencies.api.getPaywall(dto)
  }
}