package com.mercandalli.android.sdk.feature_aes_native

interface AesNativeManager {

    fun encode(
        nativeMode: AesNativeMode,
        message: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray? = null
    ): ByteArray

    fun decode(
        nativeMode: AesNativeMode,
        message: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray? = null
    ): ByteArray

    fun debug(
        string: String
    )
}
