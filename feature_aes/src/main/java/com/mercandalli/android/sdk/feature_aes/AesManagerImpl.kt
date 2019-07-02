package com.mercandalli.android.sdk.feature_aes

import com.mercandalli.android.sdk.feature_aes.internal.AesInternal

class AesManagerImpl : AesManager {

    override fun encode(messageToEncode: String, key: String): String {
        return AesInternal.encodeString(messageToEncode, key)
    }

    override fun encode(messageToEncode: ByteArray, key: ByteArray): ByteArray {
        return AesInternal.encodeByteArray(messageToEncode, key)
    }

    override fun decode(messageToDecode: String, key: String): String {
        return AesInternal.decodeString(messageToDecode, key)
    }

    override fun decode(messageToEncode: ByteArray, key: ByteArray): ByteArray {
        return AesInternal.decodeByteArray(messageToEncode, key)
    }
}
