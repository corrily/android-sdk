package com.corrily.corrilysdk

import android.util.Log

public object CorrilySDK {
  private var isAlreadyStarted = false
  fun start(apiKey: String) {
    if (isAlreadyStarted) {
      Log.d("CorrilySDK", "Corrily.start called multiple times. Please make sure you only call this once on app launch.")
      return
    }
    println("Start CorrilySDK with $apiKey")
  }



  fun requestPaywall(paywallId: String?) {}
  fun renderPaywall(paywallId: String?) {}
  fun identifyUser(userId: String?, country: String?) {}

  fun setUser(userId: String?, country: String?) {}
  fun setFallbackPaywall(jsonString: String) {}

}