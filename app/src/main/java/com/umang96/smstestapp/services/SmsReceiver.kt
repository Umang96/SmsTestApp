package com.umang96.smstestapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import androidx.work.*
import com.umang96.smstestapp.util.CommonUtil

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            CommonUtil.printLog("debugsms receiver called")
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                val smsSender = smsMessage.displayOriginatingAddress
                val smsBody = smsMessage.messageBody
                if (smsSender.isNullOrEmpty() || smsBody.isNullOrEmpty()) {
                    CommonUtil.printLog("debugsms invalid or empty sms sender/body")
                } else {
                    CommonUtil.printLog("debugsms got sms from $smsSender $smsBody")
                    val constraints: Constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val uploadWorkRequest: WorkRequest =
                        OneTimeWorkRequestBuilder<SmsUploadWorkManager>()
                            .setConstraints(constraints).build()
                    WorkManager.getInstance(context).enqueue(uploadWorkRequest)
                }
            }
        }
    }

}