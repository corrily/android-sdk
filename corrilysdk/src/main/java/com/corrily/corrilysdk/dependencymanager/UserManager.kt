package com.corrily.corrilysdk.dependencymanager

import android.content.Context
import com.corrily.corrilysdk.models.IdentifyDto
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class UserManager(private val context: Context, private val factory: DependencyProtocol) {
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

  @OptIn(DelicateCoroutinesApi::class)
  fun setUser(userId: String?, country: String?, disableIdentificationRequest: Boolean = false) {
    this.userId = userId
    if (country != null) {
      this.country = country
    }

    if (!disableIdentificationRequest && userId != null) {
      GlobalScope.launch {
        val dto = IdentifyDto(userId = userId, ip = factory.user.deviceId, country = country)
        factory.api.identifyUser(dto)
      }
    }
  }

  private fun generateRandomIpV6(): String {
    val rng = Random()
    return (1..8).joinToString(":") {
      rng.nextInt(65536).toString(16).padStart(4, '0')
    }
  }
}