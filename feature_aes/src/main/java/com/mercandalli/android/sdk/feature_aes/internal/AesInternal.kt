package com.mercandalli.android.sdk.feature_aes.internal

class AesInternal {

    companion object {

        @JvmStatic
        external fun encodeByteArray(
            messageToEncode: ByteArray,
            key: ByteArray
        ): ByteArray

        @JvmStatic
        external fun decodeByteArray(
            messageToDecode: ByteArray,
            key: ByteArray
        ): ByteArray

        init {
            System.loadLibrary("aes-lib")
        }
    }
}
