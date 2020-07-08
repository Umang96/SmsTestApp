package com.umang96.smstestapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
    private var firstResume = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.verifyUserIdentity(this)
        prepareWidgetActions()
    }

    override fun onResume() {
        super.onResume()
        if (firstResume) {
            firstResume = false
        } else {
            if (viewModel.checkSmsPermission(this)) {
                loadDataFromApiIntoRecycler()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.getItem(0)?.isVisible = viewModel.checkSmsPermission(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnRefresh) {
            if (viewModel.checkSmsPermission(this)) {
                loadDataFromApiIntoRecycler()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun prepareWidgetActions() {
        adapter = SmsAdapter()
        binding.rvSms.adapter = adapter
        binding.rvSms.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        if (viewModel.checkSmsPermission(this)) {
            loadDataFromApiIntoRecycler()
        } else {
            if (PrefUtil.getBooleanPref(this, Constants.Prefs.PREF_SMS_PERMISSION_DENIED_FOREVER)) {
                binding.tvPermissionInfo.text = getString(R.string.denied_forever_info)
            }
            binding.btnGrantPermission.setOnClickListener { viewModel.requestSmsPermission(this) }
        }
    }

    private fun loadDataFromApiIntoRecycler() {
        CommonUtil.printLog("debugpermission loading data from api")
        binding.btnGrantPermission.visibility = View.GONE
        binding.tvPermissionInfo.visibility = View.GONE
        binding.rvSms.visibility = View.VISIBLE
        binding.waitSpinner.visibility = View.VISIBLE
        invalidateOptionsMenu()
        viewModel.loadDataFromApi(this).observe(this, Observer {
            it?.also {
                CommonUtil.printLog("debugapi got response $it")
                if (it.list.isNotEmpty()) {
                    adapter?.listOfSms?.clear()
                    adapter?.listOfSms?.addAll(it.list)
                    adapter?.notifyDataSetChanged()
                } else {
                    binding.rvSms.visibility = View.GONE
                    binding.btnGrantPermission.visibility = View.VISIBLE
                    binding.tvPermissionInfo.visibility = View.VISIBLE
                    binding.btnGrantPermission.setOnClickListener { loadDataFromApiIntoRecycler() }
                    binding.btnGrantPermission.text = getString(R.string.check_again)
                    binding.tvPermissionInfo.text =
                        when {
                            it.error.orEmpty() == getString(R.string.network_err) -> it.error
                            it.error.isNullOrEmpty() -> getString(
                                R.string.no_messages
                            )
                            else -> it.error
                        }
                    invalidateOptionsMenu()
                }
            }
            binding.waitSpinner.visibility = View.GONE
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.SMS_PERMISSION_REQUEST_CODE && viewModel.checkSmsPermission(
                this
            )
        ) {
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