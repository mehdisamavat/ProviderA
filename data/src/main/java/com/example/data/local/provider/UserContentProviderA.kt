package com.example.data.local.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.example.data.entity.UserEntity
import com.example.data.local.dao.UserDao
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
                    id = values?.get(ID_KEY).toString().toInt(),
                    name = values?.get(NAME_KEY).toString(),
                    checked = values?.get(CHECKED_KEY).toString().toBoolean()
                )

                val rowId = userDao.insert(userEntity)
                val finalUri = ContentUris.withAppendedId(uri, rowId)
                context!!.contentResolver.notifyChange(finalUri, null)
                if (values?.get("from").toString() == PROVIDER_A)
                    providerManager.insertUser(
                        DOMAIN_URI_B,
                        rowId.toInt(),
                        userEntity.name,
                        userEntity.checked,
                        values?.get(FROM_KEY).toString()
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
                    id = values!!.get(ID_KEY).toString().toInt(),
                    name = values.get(NAME_KEY).toString(),
                    checked = values.get(CHECKED_KEY) as Boolean
                )

                val count = userDao.update(userEntity)
                if (count == 1) {
                    context!!.contentResolver.notifyChange(
                        ContentUris.withAppendedId(
                            uri,
                            userEntity.id.toLong()
                        ), null
                    )
                    when (values.get(FROM_KEY).toString()) {
                        PROVIDER_A -> {
                            providerManager.updateUser(
                                DOMAIN_URI_B,
                                userEntity.id,
                                userEntity.name,
                                userEntity.checked,
                                values.get(FROM_KEY).toString()
                            )
                        }
                    }
                }
                count
            }
            DOMAINS_ALL_FALSE_CODE -> {
                userDao.updateAllCheckedToFalse()
            }
            else -> 0
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> {
                val id = selectionArgs?.get(0)?.toInt()
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


    companion object {
        const val DOMAINS = "domains"
        const val DOMAINS_ITEM = "domains/#"
        const val DOMAINS_ALL_FALSE = "domainsAllFalse"

        const val DOMAINS_CODE = 10
        const val DOMAINS_ITEM_CODE = 11
        const val DOMAINS_ALL_FALSE_CODE = 12

        const val PROVIDER_A = "providerA"
        private const val PROVIDER_B = "providerB"

        const val AUTHORITY_A = "com.example.$PROVIDER_A"
        private const val AUTHORITY_B = "com.example.$PROVIDER_B"

        const val DOMAIN_URI_A = "content://$AUTHORITY_A/$DOMAINS"
        const val DOMAIN_URI_B = "content://$AUTHORITY_B/$DOMAINS"

        const val ID_KEY = "id"
        const val NAME_KEY = "name"
        const val CHECKED_KEY = "checked"
        const val FROM_KEY = "from"

    }

}