package com.example.common


object ProviderContract {
    const val DOMAINS = "domains"
    const val DOMAINS_ITEM = "domains/#"
    const val DOMAINS_ALL_FALSE = "domainsAllFalse"
    const val DOMAINS_ALL_FALSE_CODE = 12


    const val DOMAINS_CODE = 10
    const val DOMAINS_ITEM_CODE = 11

    const val AUTHORITY_A = "com.example.newprovider"
    const val AUTHORITY_B = "com.example.newprovider2"

    const val DOMAIN_STRING_URI = "content://$AUTHORITY_A/$DOMAINS"
    const val DOMAIN_STRING_URI2 = "content://$AUTHORITY_B/$DOMAINS"

    const val DOMAIN_UPDATE_URI_A = "content://$AUTHORITY_A/$DOMAINS_ALL_FALSE"
    const val DOMAIN_UPDATE_URI_B = "content://$AUTHORITY_B/$DOMAINS_ALL_FALSE"
}