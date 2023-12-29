package com.corrily.corrilysdk.dependencymanager

import android.content.Context
import java.util.Random

class UserManager(context: Context, factory: DependencyProtocol) {
  var deviceId: String
  var userId: String? = null
  var country: String

  private val deviceIdKey = "UserDeviceId"

  init {
    this.deviceId = factory.storage.read(deviceIdKey) ?: run {
      val generatedDeviceId = generateRandomIpV6()
      factory.storage.write(deviceIdKey, generatedDeviceId)
      generatedDeviceId
    }
    this.country = (CountryCodeHelper.getCodeFromTelephony(context) ?: CountryCodeHelper.getCodeFromLocale()).toString()
  }

  fun setUser(userId: String?, country: String?) {
    this.userId = userId
    if (country != null) {
      this.country = country
    }
  }

  private fun generateRandomIpV6(): String {
    val rng = Random()
    return (1..8).joinToString(":") {
      rng.nextInt(65536).toString(16).padStart(4, '0')
    }
  }
}