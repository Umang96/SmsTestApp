package com.umang96.smstestapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umang96.smstestapp.BuildConfig
import com.umang96.smstestapp.R
import com.umang96.smstestapp.api.RetrofitProvider
import com.umang96.smstestapp.api.SmsApi
import com.umang96.smstestapp.data.Request
import com.umang96.smstestapp.data.Response
import com.umang96.smstestapp.util.Constants
import com.umang96.smstestapp.util.PrefUtil
import retrofit2.Call
import retrofit2.Callback
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

class MainViewModel : ViewModel() {

    internal fun checkSmsPermission(activity: AppCompatActivity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.RECEIVE_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    internal fun requestSmsPermission(activity: AppCompatActivity) {
        if (PrefUtil.getBooleanPref(activity, Constants.Prefs.PREF_SMS_PERMISSION_DENIED_FOREVER)) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            intent.data = uri
            activity.startActivityForResult(intent, Constants.SMS_PERMISSION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECEIVE_SMS),
                Constants.SMS_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun loadDataFromApi(context: Context?): LiveData<Response?> {
        val liveData = MutableLiveData<Response?>()
        context?.also { ctx ->
            RetrofitProvider.getClient().create(SmsApi::class.java)
                .getSms(
                    Request(
                        PrefUtil.getStringPref(ctx, Constants.Prefs.PREF_UNIQUE_USER_ID),
                        true
                    )
                )
                .enqueue(object : Callback<Response> {

                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        liveData.postValue(response.body())
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        if (t is SocketTimeoutException || t is UnknownHostException) {
                            liveData.postValue(Response(error = ctx.getString(R.string.network_err)))
                        } else {
                            liveData.postValue(Response(error = ctx.getString(R.string.unknown_err)))
                        }
                    }

                })
        }
        return liveData
    }

    fun verifyUserIdentity(context: Context?) {
        context?.also { ctx ->
            if(PrefUtil.getStringPref(ctx, Constants.Prefs.PREF_UNIQUE_USER_ID).isNullOrEmpty()) {
                val uid = if(BuildConfig.DEBUG) "8b4ec62e-a606-4abf-87f2-6a7d010a9203" else UUID.randomUUID().toString()
                PrefUtil.storeStringPref(ctx, Constants.Prefs.PREF_UNIQUE_USER_ID, uid)
            }
        }
    }

}