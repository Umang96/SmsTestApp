package com.umang96.smstestapp.util

import com.umang96.smstestapp.BuildConfig

object CommonUtil {

    fun printLog(message: String) {
        if (BuildConfig.DEBUG) println(message)
    }

    fun checkSmsFitForUploading(message: String?): Boolean {
        message?.also {
            // TODO: check it for valid content
        }
        // TODO: should return false
        return true
    }
}