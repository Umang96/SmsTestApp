package com.umang96.smstestapp.data

data class Sms(var phone: String? = null,
               var message: String? = null,
               var timestamp: Long = -1L)