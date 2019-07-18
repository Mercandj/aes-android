package com.mercandalli.android.sdk.feature_aes_sample

object KeyContant {

    @JvmStatic
    val keyByteArray = createByteArray(
        0x2b,
        0x7e,
        0x15,
        0x16,
        0x28,
        0xae,
        0xd2,
        0xa6,
        0xab,
        0xf7,
        0x15,
        0x88,
        0x09,
        0xcf,
        0x4f,
        0x3c
    )

    private fun createByteArray(
        vararg ints: Int
    ) = ByteArray(ints.size) { pos -> ints[pos].toByte() }
}
