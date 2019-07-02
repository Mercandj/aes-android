package com.mercandalli.android.sdk.feature_aes

class AesModule {

    fun createAesManager(): AesManager {
        return AesManagerImpl()
    }
}
