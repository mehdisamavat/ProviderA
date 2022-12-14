package com.example.data.repository

import com.example.data.local.provider.ProviderManager
import com.example.data.local.provider.UserContentProviderA.Companion.DOMAIN_URI_A
import com.example.data.local.provider.UserContentProviderA.Companion.PROVIDER_A
import com.example.domain.repository.IContentProviderRepository

class ContentProviderRepository(private val providerManager: ProviderManager) :
    IContentProviderRepository {
    override fun deleteUser(id: Int): Int {
        return providerManager.deleteUser(DOMAIN_URI_A, id)
    }

    override fun insertUser(name: String, checked: Boolean): String? {
        return providerManager.insertUser(
            pathUrl = DOMAIN_URI_A,
            name = name,
            checked = checked,
            from = PROVIDER_A
        )
    }

    override fun updateUser(id: Int, name: String, checked: Boolean): Int {
        return providerManager.updateUser(DOMAIN_URI_A, id, name, checked, PROVIDER_A)
    }

}