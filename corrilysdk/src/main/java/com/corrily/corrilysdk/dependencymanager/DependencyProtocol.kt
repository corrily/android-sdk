package com.corrily.corrilysdk.dependencymanager

import StorageManager
import com.corrily.corrilysdk.api.API

interface DependencyProtocol {
  var config: ConfigManager
  var api: API
  var storage: StorageManager
  var user: UserManager
}