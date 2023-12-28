package com.corrily.corrilysdk.dependencymanager

import java.util.Random

class UserManager(factory: DependencyProtocol) {
  lateinit var deviceId: String
  var userId: String? = null
  var country: String? = null

  private val deviceIdKey = "UserDeviceId"

  init {
    this.deviceId = factory.storage.read(deviceIdKey) ?: run {
      val generatedDeviceId = generateRandomIpV6()
      factory.storage.write(deviceIdKey, generatedDeviceId)
      generatedDeviceId
    }
  }

  fun setUser(userId: String?, country: String?) {
    this.userId = userId
    this.country = country
  }

  private fun generateRandomIpV6(): String {
    val rng = Random()
    return (1..8).joinToString(":") {
      rng.nextInt(65536).toString(16).padStart(4, '0')
    }
  }
}