package com.example.data.provider

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import com.example.common.ProviderContract
import com.example.domain.model.User
import javax.inject.Inject

class ProviderManager(private val contentResolver: ContentResolver) {

    fun deleteUser(pathUrl: String, id: Int): Int {
        return contentResolver.delete(Uri.parse(pathUrl), "id=?", arrayOf("$id"))
    }

    fun insertUser(
        pathUrl: String,
        id: Int = 0,
        name: String,
        checked: Boolean,
        from: String
    ): String? {
        return contentResolver.insert(Uri.parse(pathUrl), ContentValues().apply {
            put("id", id)
            put("name", name)
            put("checked", checked)
            put("from", from)
        })?.path
    }

    fun updateUser(pathUrl: String, id: Int, name: String, checked: Boolean, from: String): Int {
        return contentResolver.update(Uri.parse(pathUrl), ContentValues().apply {
            put("id", id)
            put("name", name)
            put("checked", checked)
            put("from", from)
        }, null, null)
    }

    fun insertUserToA(id: Int, name: String, checked: Boolean, from: String): String? {
        return contentResolver.insert(
            Uri.parse(ProviderContract.DOMAIN_URI_A),
            ContentValues().apply {
                put("id", id)
                put("name", name)
                put("checked", checked)
                put("from", from)
            })?.path
    }

    fun updateUserToA(id: Int, name: String, checked: Boolean, from: String): Int {
        return contentResolver.update(
            Uri.parse(ProviderContract.DOMAIN_URI_A),
            ContentValues().apply {
                put("id", id)
                put("name", name)
                put("checked", checked)
                put(
                    "from", from
                )
            },
            null,
            null
        )
    }

    fun updateCheckedAllUser(to: String): Int {
        return contentResolver.update(
            Uri.parse(if (to == "B") ProviderContract.DOMAIN_UPDATE_URI_B else ProviderContract.DOMAIN_UPDATE_URI_A),
            ContentValues().apply { put("from", "ALL") },
            null,
            null
        )
    }

}