package com.umang96.smstestapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.umang96.smstestapp.R
import com.umang96.smstestapp.databinding.ItemSmsBinding

internal class SmsAdapter : RecyclerView.Adapter<SmsAdapter.ViewHolder>() {

    val listOfSms = ArrayList<Pair<String?, String?>>()

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

        init {
            tvPhone = binding.tvFrom
            tvMessage = binding.tvMessage
        }

        fun bindView(sms: Pair<String?, String?>) {
            tvPhone?.text = sms.first
            tvMessage?.text = sms.second
        }
    }

}