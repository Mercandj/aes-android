package com.mercandalli.android.sdk.feature_aes.internal

import com.mercandalli.android.sdk.feature_aes.AesManager

class AesManagerImpl : AesManager {

    override fun encode(
        messageToEncode: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray?
    ): ByteArray {
        if (initializationVector == null) {
            return AesInternal.encodeByteArray(
                messageToEncode,
                key
            )
        }
        return AesInternal.encodeByteArrayWithIv(
            messageToEncode,
            key,
            initializationVector
        )
    }

    override fun decode(
        messageToDecode: ByteArray,
        key: ByteArray,
        initializationVector: ByteArray?
    ): ByteArray {
        if (initializationVector == null) {
            return AesInternal.decodeByteArray(
                messageToDecode,
                key
            )
        }
        return AesInternal.decodeByteArrayWithIv(
            messageToDecode,
            key,
            initializationVector
        )
    }
}
