package com.example.data.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.common.ProviderContract
import com.example.common.ProviderContract.AUTHORITY_A
import com.example.common.ProviderContract.DOMAINS
import com.example.common.ProviderContract.DOMAINS_ALL_FALSE
import com.example.common.ProviderContract.DOMAINS_ALL_FALSE_CODE
import com.example.common.ProviderContract.DOMAINS_CODE
import com.example.common.ProviderContract.DOMAINS_ITEM
import com.example.common.ProviderContract.DOMAINS_ITEM_CODE
import com.example.common.ProviderContract.DOMAIN_URI_B
import com.example.data.dao.UserDao
import com.example.data.entity.UserEntity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors.fromApplication
import dagger.hilt.components.SingletonComponent


class UserContentProviderA : ContentProvider() {

    private lateinit var userDao: UserDao
    private lateinit var appContext: Context
    private lateinit var providerManager: ProviderManager

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ContentProviderEntryPoint {
        fun getDao(): UserDao
        fun getProviderManager(): ProviderManager
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY_A, DOMAINS, DOMAINS_CODE)
        addURI(AUTHORITY_A, DOMAINS_ITEM, DOMAINS_ITEM_CODE)
        addURI(AUTHORITY_A, DOMAINS_ALL_FALSE, DOMAINS_ALL_FALSE_CODE)

    }

    override fun onCreate(): Boolean {

        appContext = context?.applicationContext ?: throw IllegalStateException()
        val hiltEntryPoint = fromApplication(appContext, ContentProviderEntryPoint::class.java)
        userDao = hiltEntryPoint.getDao()
        providerManager = hiltEntryPoint.getProviderManager()

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
                userDao.selectAll()
            }
            DOMAINS_ITEM_CODE -> {
                userDao.selectById(uri.lastPathSegment!!.toInt())
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> "vnd.android.cursor.dir/$AUTHORITY_A/domains"
            DOMAINS_ITEM_CODE -> "vnd.android.cursor.item/$AUTHORITY_A/domains"
            DOMAINS_ALL_FALSE_CODE -> "vnd.android.cursor.item/$AUTHORITY_A/domainsAllFalse"

            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> {
                val userEntity = UserEntity(
                    id = values?.get("id").toString().toInt(),
                    name = values?.get("name").toString(),
                    checked = values?.get("checked").toString().toBoolean()
                )
                Log.i("mehdi", "insert    $userEntity   ${values?.get("from")}   1")

                val rowId = userDao.insert(userEntity)
                val finalUri = ContentUris.withAppendedId(uri, rowId)
                context!!.contentResolver.notifyChange(finalUri, null)
                if (values?.get("from").toString() == "A")
                    providerManager.insertUser(
                        ProviderContract.DOMAIN_URI_B,
                        rowId.toInt(),
                        userEntity.name,
                        userEntity.checked,
                        values?.get("from").toString()
                    )
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
                val userEntity = UserEntity(
                    id = values!!.get("id").toString().toInt(),
                    name = values.get("name").toString(),
                    checked = values.get("checked") as Boolean
                )
                Log.i("mehdi", "update    $userEntity   1")

                val count = userDao.update(userEntity)
                if (count == 1) {
                    context!!.contentResolver.notifyChange(
                        ContentUris.withAppendedId(
                            uri,
                            userEntity.id.toLong()
                        ), null
                    )
                    when (values.get("from").toString()) {
                        "A" -> {
                            providerManager.updateUser(
                                DOMAIN_URI_B,
                                userEntity.id,
                                userEntity.name,
                                userEntity.checked,
                                values.get("from").toString()
                            )
                        }
                    }
                }
                count
            }
            DOMAINS_ALL_FALSE_CODE -> {
                Log.i("mehdi", "update   all   ")
                userDao.updateAllCheckedToFalse()
            }
            else -> 0
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> {
                val id = selectionArgs?.get(0)?.toInt()
                Log.i("mehdi", "delete    $id   1")
                val count = userDao.deleteById(id!!)
                if (count == 1) {
                    context!!.contentResolver.notifyChange(
                        ContentUris.withAppendedId(
                            uri,
                            id.toLong()
                        ), null
                    )
                    providerManager.deleteUser(DOMAIN_URI_B, id)

                }
                count
            }
            else -> 0
        }
    }


}