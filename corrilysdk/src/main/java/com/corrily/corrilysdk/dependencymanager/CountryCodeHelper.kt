package com.corrily.corrilysdk.dependencymanager

import android.content.Context
import android.telephony.TelephonyManager
import java.util.Locale

class CountryCodeHelper {
  companion object {
    fun getCodeFromTelephony(context: Context): String? {
      val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
      return telephonyManager.networkCountryIso?.uppercase()
    }

    fun getCodeFromLocale(): String {
      return Locale.getDefault().country.uppercase()
    }
  }
}