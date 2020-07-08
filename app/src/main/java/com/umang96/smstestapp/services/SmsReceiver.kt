package com.umang96.smstestapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.umang96.smstestapp.util.CommonUtil

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            CommonUtil.printLog("debugsms receiver called")
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                val smsSender = smsMessage.displayOriginatingAddress
                val smsBody = smsMessage.messageBody
                CommonUtil.printLog("debugsms got sms from $smsSender $smsBody")
            }
        }
    }

}