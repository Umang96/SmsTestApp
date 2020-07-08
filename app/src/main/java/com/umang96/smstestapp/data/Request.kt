package com.umang96.smstestapp.data

import com.umang96.smstestapp.util.Constants

data class Request(var userId: String? = null,
                   var getList: Boolean = false,
                   var smsData: Sms = Sms(),
                   var startCount: Int = Constants.DEFAULT_START_COUNT,
                   var count: Int = Constants.DEFAULT_RESULTS_LIMIT)