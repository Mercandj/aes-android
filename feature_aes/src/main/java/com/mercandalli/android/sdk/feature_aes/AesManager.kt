package com.mercandalli.android.sdk.feature_aes

interface AesManager {

    fun encode(
        messageToEncode: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray? = null
    ): ByteArray

    fun decode(
        messageToDecode: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray? = null
    ): ByteArray
}
