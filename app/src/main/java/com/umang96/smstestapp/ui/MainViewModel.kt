package com.umang96.smstestapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.umang96.smstestapp.BuildConfig
import com.umang96.smstestapp.util.Constants
import com.umang96.smstestapp.util.PrefUtil

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

}