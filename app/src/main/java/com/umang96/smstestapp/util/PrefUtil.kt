package com.umang96.smstestapp.util

import android.content.Context

object PrefUtil {

    private const val sharedPrefsName = "sms_test"

    fun storeBooleanPref(context: Context?, prefName: String, value: Boolean) {
        context?.also { ctx ->
            ctx.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)
                .edit().putBoolean(prefName, value).apply()
        }
    }

    fun getBooleanPref(context: Context?, prefName: String): Boolean {
        context?.also { ctx ->
            return ctx.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)
                .getBoolean(prefName, false)
        }
        return false
    }
}