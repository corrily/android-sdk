package com.corrily.corrilysdk.dependencymanager

import StorageManager
import android.content.Context
import com.corrily.corrilysdk.api.API

class DependencyManager(context: Context): DependencyProtocol {
  override var config: ConfigManager = ConfigManager()
  override var api: API = API(factory = this)
  override var storage: StorageManager = StorageManager(context = context)
  override var user: UserManager = UserManager(factory = this)
}