package com.mercandalli.sdk.feature_aes

import com.mercandalli.sdk.feature_aes.internal.AesManagerImpl

class AesModule {

    fun createAesManager(): AesManager {
        return AesManagerImpl()
    }
}
