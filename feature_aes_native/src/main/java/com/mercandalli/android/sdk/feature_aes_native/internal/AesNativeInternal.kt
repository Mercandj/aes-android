package com.mercandalli.android.sdk.feature_aes_native.internal

class AesNativeInternal {

    companion object {

        @JvmStatic
        external fun encodeByteArray(
            message: ByteArray,
            key: ByteArray,
            initializationVector: ByteArray?
        ): ByteArray

        @JvmStatic
        external fun decodeByteArray(
            message: ByteArray,
            key: ByteArray,
            initializationVector: ByteArray?
        ): ByteArray

        @JvmStatic
        external fun debug(
            debug: String
        )

        init {
            System.loadLibrary("aes-lib")
        }
    }
}
