package com.mercandalli.android.sdk.feature_aes

import com.mercandalli.android.sdk.feature_aes.internal.AesManagerImpl

class AesModule {

    fun createAesManager(): AesManager {
        return AesManagerImpl()
    }
}
