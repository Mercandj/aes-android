package com.mercandalli.sdk.feature_aes_kotlin

import com.mercandalli.sdk.feature_aes_kotlin.internal.AesManagerImpl

class AesModule {

    fun createAesManager(): AesManager {
        return AesManagerImpl()
    }
}
