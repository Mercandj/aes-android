package com.mercandalli.android.sdk.feature_aes.internal

import com.mercandalli.android.sdk.feature_aes.AesManager
import com.mercandalli.android.sdk.feature_aes.AesMode

class AesManagerImpl : AesManager {

    override fun encode(
        mode: AesMode,
        message: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray?
    ): ByteArray {
        return AesInternal.encodeByteArray(
            message,
            key,
            initializationVector
        )
    }

    override fun decode(
        mode: AesMode,
        message: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray?
    ): ByteArray {
        return AesInternal.decodeByteArray(
            message,
            key,
            initializationVector
        )
    }
}
