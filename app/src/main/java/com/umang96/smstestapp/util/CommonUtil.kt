package com.umang96.smstestapp.util

import com.umang96.smstestapp.BuildConfig

object CommonUtil {
    fun printLog(message: String) {
        if (BuildConfig.DEBUG) println(message)
    }
}