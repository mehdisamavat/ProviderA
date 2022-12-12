package com.example.newprovider.provider

import android.net.Uri
import android.provider.BaseColumns
import com.example.newprovider.Constants

object ProviderContract {
    const val AUTHORITY = "com.example.newprovider"
    val AUTHORITY_URI = Uri.parse("content://$AUTHORITY")!!

    object DomainEntry : BaseColumns {
        val TABLE_NAME = "DeviceUser"
        val DOMAIN_URI = Uri.withAppendedPath(AUTHORITY_URI, Constants.DOMAINS)
        val TITLE = "title"
    }

}