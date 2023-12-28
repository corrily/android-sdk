package com.corrily.corrilysdk.dependencymanager

import com.corrily.corrilysdk.api.API

interface DependencyProtocol {
  var config: ConfigManager
  var api: API
}