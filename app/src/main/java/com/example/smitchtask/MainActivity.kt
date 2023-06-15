package com.example.smitchtask

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.smitchtask.databinding.ActivityMainBinding
import com.example.smitchtask.ui.main.MyAdapter
import com.example.smitchtask.ui.main.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private  val binding  by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    private val viewModel: MyViewModel by viewModels()
    private lateinit var myAdapter: MyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpViewListener()

    }

    private fun setUpViewListener() {
        myAdapter= MyAdapter()
        binding.buttonOne.setOnClickListener {
            Log.e("TAG", "setUpViewListener:click 1 " )
            val serviceName = "MyService001"
            val serviceType = "_http._tcp"
            val servicePort = 80
           viewModel.publishService(serviceName, serviceType, servicePort)
        }
        viewModel.scanResults.flowWithLifecycle(lifecycle)
            .onEach {
                myAdapter.setScanResults(it)
                Log.e("TAG", "setUpViewListener: "+it.toString() )
            }.launchIn(lifecycleScope)

        binding.buttonTwo.setOnClickListener {
            val serviceType = "_http._tcp"
            viewModel.startScan(serviceType)
        }

        binding.recyclerView.apply {
            adapter=myAdapter
        }

        binding.buttonThree.setOnClickListener {

        }
    }




}