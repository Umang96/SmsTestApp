package com.umang96.smstestapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.umang96.smstestapp.R
import com.umang96.smstestapp.databinding.ActivityMainBinding
import com.umang96.smstestapp.util.CommonUtil
import com.umang96.smstestapp.util.Constants
import com.umang96.smstestapp.util.PrefUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var adapter: SmsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        prepareWidgetActions()
    }

    private fun prepareWidgetActions() {
        if (viewModel.checkSmsPermission(this)) {
            binding.btnGrantPermission.visibility = View.GONE
            binding.tvPermissionInfo.visibility = View.GONE
            loadDataFromApiIntoRecycler()
        } else {
            if (PrefUtil.getBooleanPref(this, Constants.Prefs.PREF_SMS_PERMISSION_DENIED_FOREVER)) {
                binding.tvPermissionInfo.text = getString(R.string.denied_forever_info)
            }
            binding.btnGrantPermission.setOnClickListener { viewModel.requestSmsPermission(this) }
        }
        adapter = SmsAdapter()
        binding.rvSms.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSms.adapter = adapter
    }

    private fun loadDataFromApiIntoRecycler() {
        CommonUtil.printLog("debugpermission ready to go")
        binding.btnGrantPermission.visibility = View.GONE
        binding.tvPermissionInfo.visibility = View.GONE
        viewModel.loadDataFromApi(this).observe(this, Observer {
            it?.also {
                if(it.list.isNotEmpty()) {
                    adapter?.listOfSms?.addAll(it.list)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.SMS_PERMISSION_REQUEST_CODE && viewModel.checkSmsPermission(this)) {
            loadDataFromApiIntoRecycler()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CommonUtil.printLog("debugpermission user has granted permission")
                loadDataFromApiIntoRecycler()
            } else {
                CommonUtil.printLog("debugpermission user has denied permission")
                if (!shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {
                    CommonUtil.printLog("debugpermission user has denied permission forever")
                    binding.tvPermissionInfo.text = getString(R.string.denied_forever_info)
                    PrefUtil.storeBooleanPref(
                        this,
                        Constants.Prefs.PREF_SMS_PERMISSION_DENIED_FOREVER,
                        true
                    )
                }
            }
        }
    }

}