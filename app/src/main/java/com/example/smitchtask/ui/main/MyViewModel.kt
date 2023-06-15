package com.example.smitchtask.ui.main

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.smitchtask.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class MyViewModel @Inject constructor(
    private val application:MyApplication
):ViewModel() {
    private val nsdManager: NsdManager by lazy {
        application.applicationContext.getSystemService(Context.NSD_SERVICE) as NsdManager
    }



    private val _scanResults = MutableStateFlow<List<NsdServiceInfo>>(emptyList())
    val scanResults: StateFlow<List<NsdServiceInfo>>
        get() = _scanResults

    fun publishService(serviceName: String, serviceType: String, servicePort: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val serviceInfo = NsdServiceInfo().apply {
                this.serviceName = serviceName
                this.serviceType = serviceType
                this.port = servicePort
            }
            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, null)
        }
    }


    fun startScan(serviceType: String) {
        val discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                // Handle start discovery failure
                Log.e("TAG", "onStartDiscoveryFailed: "+serviceType )
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                // Handle stop discovery failure
            }

            override fun onDiscoveryStarted(serviceType: String) {
                // Discovery started
            }

            override fun onDiscoveryStopped(serviceType: String) {
                // Discovery stopped
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                if (serviceInfo.serviceType == serviceType) {
                    val updatedList = _scanResults.value.toMutableList()
                    updatedList.add(serviceInfo)
                    _scanResults.value = updatedList
                }
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                if (serviceInfo.serviceType == serviceType) {
                    val updatedList = _scanResults.value.toMutableList()
                    updatedList.remove(serviceInfo)
                    _scanResults.value = updatedList
                }
                Log.e("TAG", "onStartDiscoveryFailed: "+serviceType )

            }
        }

        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }
}