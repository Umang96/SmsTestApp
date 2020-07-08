package com.umang96.smstestapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.umang96.smstestapp.R
import com.umang96.smstestapp.data.Sms
import com.umang96.smstestapp.databinding.ItemSmsBinding
import java.util.*
import kotlin.collections.ArrayList

internal class SmsAdapter : RecyclerView.Adapter<SmsAdapter.ViewHolder>() {

    val listOfSms = ArrayList<Sms>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_sms,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfSms.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfSms[position])
    }

    internal class ViewHolder(binding: ItemSmsBinding) : RecyclerView.ViewHolder(binding.root) {

        private var tvPhone: TextView? = null
        private var tvMessage: TextView? = null
        private var tvTime: TextView? = null

        init {
            tvPhone = binding.tvFrom
            tvMessage = binding.tvMessage
            tvTime = binding.tvTime
        }

        fun bindView(sms: Sms) {
            tvPhone?.text = sms.phone
            tvMessage?.text = sms.message
            val cal = Calendar.getInstance()
            cal.timeInMillis = sms.timestamp
            val dateStr = "${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.YEAR)}"
            tvTime?.text = dateStr
        }
    }

}