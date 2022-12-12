package com.example.newprovider.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.newprovider.DeviceUserHelper
import com.example.newprovider.DomainData
import com.example.newprovider.R
import com.example.newprovider.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)




        val resolverHelper = DeviceUserHelper(this, Handler(Looper.getMainLooper()))


        resolverHelper.titleLiveData.observe(this) {
            binding.textview.text = "Size: ${it.size}"
        }

        binding.btnDomain.setOnClickListener {
            lifecycleScope.launch {
                resolverHelper.insertDomain(DomainData(title = "Hey"))
            }
        }



    }
}