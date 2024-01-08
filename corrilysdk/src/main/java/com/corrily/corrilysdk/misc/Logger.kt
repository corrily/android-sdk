package com.corrily.corrilysdk.misc

import android.util.Log

object Logger {

  private enum class LogLevel(val prefix: String) {
    Info("[Info \uD83D\uDCAC]"),
    Warn("[Warning ⚠\uFE0F]"),
    Error("[Error ⁉\uFE0F]")
  }

  private fun log(logLevel: LogLevel, tag: String, message: String) {
    when (logLevel) {
      LogLevel.Info -> Log.i(tag, "${logLevel.prefix} $message")
      LogLevel.Warn -> Log.w(tag, "${logLevel.prefix} $message")
      LogLevel.Error -> Log.e(tag, "${logLevel.prefix} $message")
    }
  }

  fun info(tag: String, message: String) {
    log(LogLevel.Info, tag, message)
  }

  fun warn(tag: String, message: String) {
    log(LogLevel.Warn, tag, message)
  }

  fun error(tag: String, message: String) {
    log(LogLevel.Error, tag, message)
  }
}