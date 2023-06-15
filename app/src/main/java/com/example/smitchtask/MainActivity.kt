package com.example.smitchtask

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.smitchtask.databinding.ActivityMainBinding
import com.example.smitchtask.ui.ble.BluetoothListAdapter
import com.example.smitchtask.ui.ble.DeviceInfoData
import com.example.smitchtask.ui.main.MyAdapter
import com.example.smitchtask.ui.main.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.io.IOException
import java.util.UUID

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MyViewModel by viewModels()
    private lateinit var myAdapter: MyAdapter
    private val bluetoothListAdapter = BluetoothListAdapter(listener = ::onItemClick)
    private lateinit var bluetoothDevice: BluetoothDevice

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var deviceInfoList = arrayListOf<BluetoothDevice>()
    private var deviceList = arrayListOf<DeviceInfoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpViewListener()

    }

    private fun setUpViewListener() {
        myAdapter = MyAdapter()
        binding.buttonOne.setOnClickListener {
            val serviceName = "MyService001"
            val serviceType = "_http._tcp"
            val servicePort = 80
            viewModel.publishService(serviceName, serviceType, servicePort)
        }
        viewModel.scanResults.flowWithLifecycle(lifecycle)
            .onEach {
                if (it.isNotEmpty()) {
                    myAdapter.setScanResults(it)
//                    binding.recyclerView.apply {
//                        adapter = myAdapter
//                    }
                }
            }.launchIn(lifecycleScope)

        binding.buttonTwo.setOnClickListener {
            val serviceType = "_http._tcp"
            viewModel.startScan(serviceType)
        }


        binding.buttonThree.setOnClickListener {
            if (bluetoothAdapter == null) {
                initBluetoothDiscovery()
            }
            startDiscovery()
        }
    }


    private fun initBluetoothDiscovery() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        val intentFilter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        registerReceiver(bluetoothReceiver, intentFilter)
    }


    private val bluetoothReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val device =
                            intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        device?.let {
                            deviceInfoList.add(it)
                            deviceList.add(DeviceInfoData("", it.name, it.address))
                        }
                    } else {
                        // Handle the case where the permission is not granted
                        Timber.d("Bluetooth permission not granted.")
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.e("TAG", "onReceive: $deviceInfoList")
                    bluetoothListAdapter.submitList(deviceInfoList)
                    binding.recyclerView.apply {
                        adapter = bluetoothListAdapter
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startDiscovery() {
        if (bluetoothAdapter?.isDiscovering == true) {
            Timber.d("stop discovery")
            bluetoothAdapter?.cancelDiscovery()
        }
        Timber.d("start discovery, show loading")
        bluetoothAdapter?.startDiscovery()
    }


    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        // Establish a connection with the device
        // Implement your connection logic here

        // Example connection logic:
        val uuid: UUID = UUID.randomUUID()
        val socket: BluetoothSocket? = device.createRfcommSocketToServiceRecord(uuid)
        try {

            Timber.d("value of socket is $uuid")
            Timber.d("value of socket is ${socket?.isConnected}")
            socket?.connect()
            // Connection successful, perform further actions if needed
        } catch (e: IOException) {

            // Handle connection failure
            e.printStackTrace()
        }
    }


    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        bluetoothAdapter?.cancelDiscovery();
        unregisterReceiver(bluetoothReceiver);
    }

    @SuppressLint("MissingPermission")
    private fun onItemClick(deviceInfoData: BluetoothDevice) {
        Timber.d("item is clicked")
        connectToDevice(deviceInfoData)
        if (bluetoothAdapter!!.isEnabled) {
            // Bluetooth is enabled
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter!!.bondedDevices

            if (!pairedDevices.isNullOrEmpty()) {
                // Loop through the paired devices to find the one you want to connect to
                for (device in pairedDevices) {
                    if (device.name == "Your Device Name") {
                        bluetoothDevice = device
                        Timber.d("uuid is ${bluetoothDevice.name}")

                        break
                    }
                }
            }
        } else {
            // Bluetooth is not enabled, request user to enable Bluetooth
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            Timber.d("uuid is ${deviceInfoData.uuids}")
        }
    }


}