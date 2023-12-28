package com.corrily.corrilysdk.dependencymanager

import java.util.Random

class UserManager {
  lateinit var deviceId: String
  var userId: String? = null
  var country: String? = null

  init {
    this.deviceId = generateRandomIpV6()
  }

  fun setUser(userId: String?, country: String?) {
    this.userId = userId
    this.country = country
  }

  fun generateRandomIpV6(): String {
    val rng = Random()
    return (1..8).joinToString(":") {
      rng.nextInt(65536).toString(16).padStart(4, '0')
    }
  }
}