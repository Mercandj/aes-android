package com.mercandalli.android.sdk.feature_aes

interface AesManager {

    fun encode(
        mode: AesMode,
        message: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray? = null
    ): ByteArray

    fun decode(
        mode: AesMode,
        message: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray? = null
    ): ByteArray
}
