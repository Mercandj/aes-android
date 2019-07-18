package com.mercandalli.sdk.feature_aes_kotlin

import com.mercandalli.sdk.feature_aes_kotlin.internal.AesKotlinManagerImpl

class AesKotlinModule {

    fun createAesManager(): AesKotlinManager {
        return AesKotlinManagerImpl()
    }
}
