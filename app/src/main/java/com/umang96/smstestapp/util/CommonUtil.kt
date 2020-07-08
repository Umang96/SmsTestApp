package com.umang96.smstestapp.util

import com.umang96.smstestapp.BuildConfig

object CommonUtil {

    private val listOfMonths =
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    fun printLog(message: String) {
        if (BuildConfig.DEBUG) println(message)
    }

    fun checkSmsFitForUploading(message: String?): Boolean {
        message?.also {
            // check if message contains three double digit numbers
            if (it.matches(Regex(".*\\d{2}.*\\d{2}.*\\d{2}.*"))) {
                printLog("contains two dual digit numbers")
                // check if sms message contains d-*-d or d/*/d
                if (it.matches(Regex(".*\\d-.*-\\d.*")) ||
                    it.matches(Regex(".*\\d/.*/\\d.*")) ||
                    checkMsgContainsMonth(it)) {
                    printLog("contains date and amount")
                    return true
                }
            }
        }
        return false
    }

    private fun checkMsgContainsMonth(message: String): Boolean {
        for (month in listOfMonths) {
            if (message.contains(month, false)) return true
        }
        return false
    }

}