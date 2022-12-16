package com.example.data.local.provider

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import com.example.data.local.provider.UserContentProviderA.Companion.CHECKED_KEY
import com.example.data.local.provider.UserContentProviderA.Companion.FROM_KEY
import com.example.data.local.provider.UserContentProviderA.Companion.ID_KEY
import com.example.data.local.provider.UserContentProviderA.Companion.NAME_KEY

class ProviderManager(private val contentResolver: ContentResolver) {

    fun deleteUser(pathUrl: String, id: Int): Int {
        return contentResolver.delete(Uri.parse(pathUrl), "$ID_KEY=?", arrayOf("$id"))
    }

    fun insertUser(
        pathUrl: String,
        id: Int = 0,
        name: String,
        checked: Boolean,
        from: String
    ): String? {
        return contentResolver.insert(Uri.parse(pathUrl), ContentValues().apply {
            put(ID_KEY, id)
            put(NAME_KEY, name)
            put(CHECKED_KEY, checked)
            put(FROM_KEY, from)
        })?.path
    }

    fun updateUser(pathUrl: String, id: Int, name: String, checked: Boolean, from: String): Int {
        return contentResolver.update(Uri.parse(pathUrl), ContentValues().apply {
            put(ID_KEY, id)
            put(NAME_KEY, name)
            put(CHECKED_KEY, checked)
            put(FROM_KEY, from)
        }, null, null)
    }





}