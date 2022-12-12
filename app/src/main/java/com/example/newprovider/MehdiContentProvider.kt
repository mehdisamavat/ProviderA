package com.example.newprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.newprovider.Constants.DOMAINS
import com.example.newprovider.Constants.DOMAINS_CODE
import com.example.newprovider.Constants.DOMAINS_ITEM
import com.example.newprovider.Constants.DOMAINS_ITEM_CODE
import com.example.newprovider.database.DeviceUserDao
import com.example.newprovider.database.MainDatabase
import com.example.newprovider.database.DeviceUser

const val PROVIDER = "com.example.newprovider"

class MehdiContentProvider : ContentProvider() {

    private lateinit var database: MainDatabase
    private lateinit var deviceUserDao: DeviceUserDao


    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(PROVIDER, DOMAINS, DOMAINS_CODE)
        addURI(PROVIDER, DOMAINS_ITEM, DOMAINS_ITEM_CODE)
    }

    override fun onCreate(): Boolean {
        database = MainDatabase.getInstance(context!!)
        deviceUserDao = database.domainDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {

            DOMAINS_CODE -> {
                deviceUserDao.selectAll()
            }

            DOMAINS_ITEM_CODE -> {
                deviceUserDao.selectById(uri.lastPathSegment!!.toInt())
            }

            else -> null
        }

    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> "vnd.android.cursor.dir/$PROVIDER/domains"
            DOMAINS_ITEM_CODE -> "vnd.android.cursor.item/$PROVIDER/domains"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> {
                val domain = DeviceUser(
                    id = 0,
                    branch = "2222",
                    enrollment = "2222",
                    imei = "222",
                    dataConfig = "222",
                    configuratoorEnrollment = "2222",
                    dateUpdate = null,
                    accepted = false,
                    name = values?.get("title").toString()
                )
                val rowId = deviceUserDao.insert(domain)
                val finalUri = ContentUris.withAppendedId(uri, rowId)
                context!!.contentResolver.notifyChange(finalUri, null)
                finalUri
            }
            else -> null
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> {
                val domain =
                    DeviceUser(id = values!!.get("id") as Int,
                        branch = "222",
                        enrollment = "2222",
                        imei = "222",
                        dataConfig = "222",
                        configuratoorEnrollment = "222",
                        dateUpdate = null,
                        accepted = false,
                        name = null)

                val count = deviceUserDao.update(domain)
                if (count == 1) {
                    context!!.contentResolver.notifyChange(
                        ContentUris.withAppendedId(
                            uri,
                            domain.id.toLong()
                        ), null
                    )
                }
                count
            }
            else -> 0
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            DOMAINS_ITEM_CODE -> {
                val rowId = uri.lastPathSegment!!.toInt()
                val count = deviceUserDao.deleteById(rowId)
                if (count == 1) {
                    context!!.contentResolver.notifyChange(
                        ContentUris.withAppendedId(
                            uri,
                            rowId.toLong()
                        ), null
                    )
                }
                count
            }
            else -> 0
        }
    }
}