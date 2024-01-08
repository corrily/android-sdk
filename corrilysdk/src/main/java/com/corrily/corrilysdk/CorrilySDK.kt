package com.corrily.corrilysdk

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import com.corrily.corrilysdk.dependencymanager.DependencyManager
import com.corrily.corrilysdk.dependencymanager.DependencyProtocol
import com.corrily.corrilysdk.models.IdentifyDto
import com.corrily.corrilysdk.models.IdentifyResponse
import com.corrily.corrilysdk.models.PaywallDto
import com.corrily.corrilysdk.models.PaywallResponse
import com.corrily.corrilysdk.views.PaywallView

object CorrilySDK {
  private var initialized = false

  private lateinit var dependencies: DependencyManager

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

  @Composable
  fun RenderPaywall(activity: Activity, CustomView: (@Composable (DependencyProtocol) -> Unit)? = null) {
    if (CustomView != null) {
      CustomView(dependencies)
    } else {
      PaywallView(activity = activity, factory = dependencies)
    }
  }

  fun setFallbackPaywall(jsonString: String) {
    dependencies.paywall.setFallbackPaywall(jsonString)
  }

  fun setUser(userId: String?, country: String?) {
    dependencies.user.setUser(userId, country)
  }

  suspend fun identifyUser(userId: String, country: String?): IdentifyResponse {
    val dto = IdentifyDto(userId, ip = dependencies.user.deviceId, country)
    return dependencies.api.identifyUser(dto)
  }

  suspend fun requestPaywall(paywallId: Int?): PaywallResponse {
    val dto = PaywallDto(
      country = dependencies.user.country,
      userId = dependencies.user.userId,
      ip = dependencies.user.deviceId,
      paywallId = paywallId
    )
    return dependencies.api.getPaywall(dto)
  }
}