package com.mercandalli.android.sdk.feature_aes.internal

class AesInternal {

    companion object {

        @JvmStatic
        external fun encodeByteArray(
            message: ByteArray,
            key: ByteArray,
            initializationVector: ByteArray?
        ):ByteArray

        @JvmStatic
        external fun decodeByteArray(
            message: ByteArray,
            key: ByteArray,
            initializationVector: ByteArray?
        ):ByteArray

        init {
            System.loadLibrary("aes-lib")
        }
    }
}
