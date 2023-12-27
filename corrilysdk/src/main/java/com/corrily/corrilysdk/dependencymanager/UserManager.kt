package com.corrily.corrilysdk.dependencymanager

class UserManager {
  var deviceId: String = "aaa"
  var userId: String? = null
  var country: String? = null

  fun setUser(userId: String?, country: String?) {
    this.userId = userId
    this.country = country
  }
}