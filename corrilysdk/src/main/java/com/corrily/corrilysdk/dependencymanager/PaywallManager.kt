package com.corrily.corrilysdk.dependencymanager

import android.util.Log
import com.corrily.corrilysdk.misc.JSON
import com.corrily.corrilysdk.models.PaywallResponse

class PaywallManager {
  var fallbackPaywall: PaywallResponse? = null

  fun setFallbackPaywall(jsonString: String) {
    try {
      fallbackPaywall = JSON.decodeFromString<PaywallResponse>(jsonString)
    } catch (e: Exception) {
      e.message?.let { Log.d("PaywallManager", it) }
    }
  }
}