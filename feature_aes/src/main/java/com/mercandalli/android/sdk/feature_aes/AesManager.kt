package com.mercandalli.android.sdk.feature_aes

interface AesManager {

    fun encode(messageToEncode: ByteArray, key: ByteArray): ByteArray

    fun decode(messageToEncode: ByteArray, key: ByteArray): ByteArray
}
