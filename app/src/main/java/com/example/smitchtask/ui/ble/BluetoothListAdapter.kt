package com.example.smitchtask.ui.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smitchtask.databinding.HolderItemListLayoutBinding

class BluetoothListAdapter(
    val listener: (BluetoothDevice) -> Unit,
) : ListAdapter<BluetoothDevice, BluetoothListAdapter.EmiDetailViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmiDetailViewHolder {
        val binding = HolderItemListLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EmiDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmiDetailViewHolder, position: Int) {
        with(getItem(position)) {
            holder.bind(this, position) { listener(this) }
        }
    }

    inner class EmiDetailViewHolder(private val itemBinding: HolderItemListLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("MissingPermission")
        fun bind(deviceInfo: BluetoothDevice, position: Int, listener: () -> Unit) {
            itemBinding.deviceName.text = deviceInfo.name
            itemBinding.root.setOnClickListener{
                listener()
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BluetoothDevice>() {
            override fun areItemsTheSame(
                oldItem: BluetoothDevice,
                newItem: BluetoothDevice,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: BluetoothDevice,
                newItem: BluetoothDevice,
            ): Boolean {
                return oldItem.address!! == newItem.address!!
            }

        }
    }
}