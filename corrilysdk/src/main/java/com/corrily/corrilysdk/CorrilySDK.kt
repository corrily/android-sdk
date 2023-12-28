package com.corrily.corrilysdk

import android.content.Context
import android.util.Log
import com.corrily.corrilysdk.dependencymanager.DependencyManager

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
}