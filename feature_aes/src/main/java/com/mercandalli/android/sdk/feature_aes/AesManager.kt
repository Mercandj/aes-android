package com.mercandalli.android.sdk.feature_aes

interface AesManager {

    fun encode(messageToEncode: String, key: String): String

    fun encode(messageToEncode: ByteArray, key: ByteArray): ByteArray

    fun decode(messageToDecode: String, key: String): String

    fun decode(messageToEncode: ByteArray, key: ByteArray): ByteArray
}
