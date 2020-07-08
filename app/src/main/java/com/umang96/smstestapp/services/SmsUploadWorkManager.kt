package com.umang96.smstestapp.services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.umang96.smstestapp.api.RetrofitProvider
import com.umang96.smstestapp.api.SmsApi
import com.umang96.smstestapp.data.Request
import com.umang96.smstestapp.data.Response
import com.umang96.smstestapp.util.Constants
import com.umang96.smstestapp.util.PrefUtil
import retrofit2.Call
import retrofit2.Callback

class SmsUploadWorkManager(var context: Context, var parameters: WorkerParameters) : Worker(context, parameters) {

    override fun doWork(): Result {
        RetrofitProvider.getClient().create(SmsApi::class.java)
            .putSms(Request(PrefUtil.getStringPref(context, Constants.Prefs.PREF_UNIQUE_USER_ID),
                false, Pair(parameters.inputData.getString("sender"),
                    parameters.inputData.getString("message"))))
            .enqueue(object : Callback<Response> {

                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    // tell local db that sms is uploaded
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    // tell local db that sms failed to upload
                }

            })

        return Result.success()
    }
}