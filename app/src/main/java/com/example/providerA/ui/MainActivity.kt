package com.example.providerA.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.providerA.R
import com.example.providerA.databinding.ActivityMainBinding
import com.example.providerB.IMyAidlInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ServiceConnection {

    lateinit var binding: ActivityMainBinding
    var isServiceConnected = false
    var iMyAidlInterface: IMyAidlInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        binding.create.setOnClickListener {
            if (!isServiceConnected) {
                val intent = Intent()
                intent.component =
                    ComponentName("com.example.providerB", "com.example.providerB.ConsumerService")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
                this.bindService(intent, this, BIND_AUTO_CREATE)
            } else {
                try {
                    iMyAidlInterface?.getData(Random.nextInt(0, 100).toString(), false)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }

    }


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        isServiceConnected = true
        iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service)
        try {
            iMyAidlInterface?.getData(Random.nextInt(0, 100).toString(), false)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        isServiceConnected = false
        try {
            iMyAidlInterface?.stopService()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }


}