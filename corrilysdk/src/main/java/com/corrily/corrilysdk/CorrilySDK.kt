package com.corrily.corrilysdk

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import com.corrily.corrilysdk.dependencymanager.DependencyManager
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
  fun RenderPaywall() {
    PaywallView(factory = dependencies)
  }
}