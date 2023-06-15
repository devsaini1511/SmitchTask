package com.example.smitchtask.ui.main

import android.net.nsd.NsdServiceInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smitchtask.R

class MyAdapter : RecyclerView.Adapter<MyAdapter.ScanResultViewHolder>() {

    private val scanResults: MutableList<NsdServiceInfo> = mutableListOf()

    fun setScanResults(results: List<NsdServiceInfo>) {
        scanResults.clear()
        scanResults.addAll(results)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_item_layout, parent, false)
        return ScanResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanResultViewHolder, position: Int) {
        val scanResult = scanResults[position]
        holder.bind(scanResult)
    }

    override fun getItemCount(): Int {
        return scanResults.size
    }

    inner class ScanResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceNameTextView: TextView = itemView.findViewById(R.id.Name)

        fun bind(scanResult: NsdServiceInfo) {
            serviceNameTextView.text = scanResult.serviceName
        }
    }
}
