package com.mercandalli.android.sdk.feature_aes.internal

class AesInternal {

    companion object {

        @JvmStatic
        external fun encodeByteArray(
            messageToEncode: ByteArray,
            key: ByteArray
        ): ByteArray

        @JvmStatic
        external fun encodeByteArrayWithIv(
            messageToEncode: ByteArray,
            key: ByteArray,
            initializationVector: ByteArray
        ): ByteArray

        @JvmStatic
        external fun decodeByteArray(
            messageToDecode: ByteArray,
            key: ByteArray
        ): ByteArray

        @JvmStatic
        external fun decodeByteArrayWithIv(
            messageToDecode: ByteArray,
            key: ByteArray,
            initializationVector: ByteArray
        ): ByteArray

        init {
            System.loadLibrary("aes-lib")
        }
    }
}
