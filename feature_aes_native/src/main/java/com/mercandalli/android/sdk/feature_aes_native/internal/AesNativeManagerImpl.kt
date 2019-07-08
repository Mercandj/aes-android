package com.mercandalli.android.sdk.feature_aes_native.internal

import com.mercandalli.android.sdk.feature_aes_native.AesNativeManager
import com.mercandalli.android.sdk.feature_aes_native.AesNativeMode

class AesNativeManagerImpl : AesNativeManager {

    override fun encode(
        nativeMode: AesNativeMode,
        message: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray?
    ): ByteArray {
        return AesNativeInternal.encodeByteArray(
            message,
            key,
            initializationVector
        )
    }

    override fun decode(
        nativeMode: AesNativeMode,
        message: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray?
    ): ByteArray {
        return AesNativeInternal.decodeByteArray(
            message,
            key,
            initializationVector
        )
    }

    override fun debug(string: String) {
        AesNativeInternal.debug(string)
    }
}
