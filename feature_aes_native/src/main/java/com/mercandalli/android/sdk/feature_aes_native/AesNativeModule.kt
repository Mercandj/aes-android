package com.mercandalli.android.sdk.feature_aes_native

import com.mercandalli.android.sdk.feature_aes_native.internal.AesNativeManagerImpl

class AesNativeModule {

    fun createAesManager(): AesNativeManager {
        return AesNativeManagerImpl()
    }
}
