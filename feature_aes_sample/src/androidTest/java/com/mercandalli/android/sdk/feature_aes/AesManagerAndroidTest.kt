package com.mercandalli.android.sdk.feature_aes

import org.junit.Assert
import org.junit.Test

class AesManagerAndroidTest {

    private val keyByteArray = createByteArray(
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

    private val messageByteArray = createByteArray(
        0x6b,
        0xc1,
        0xbe,
        0xe2,
        0x2e,
        0xe9,
        0xbe,
        0xe2,
        0x3d,
        0x7e,
        0x11,
        0x73,
        0xbe,
        0xe2,
        0x93,
        0x40,
        0xe9,
        0x3d,
        0x7e,
        0xbe,
        0xe2,
        0xe9,
        0x3d,
        0x7e,
        0x11,
        0x73,
        0x93,
        0x11,
        0xbe,
        0xe2,
        0xbe,
        0xe2,
        0xbe,
        0xe2,
        0x73,
        0x93,
        0xe9,
        0x3d,
        0x7e,
        0x11,
        0x73,
        0x93,
        0x9f,
        0x96,
        0xbe,
        0xe2,
        0xbe,
        0xe2,
        0xe9,
        0x3d,
        0x7e,
        0x11,
        0x73,
        0x93,
        0x17,
        0x2a
    )

    @Test
    fun encodeDecodeByteArray() {
        // Given
        val aesManager = AesModule().createAesManager()

        // When
        val encodedByteArray = aesManager.encode(
            AesMode.ECB,
            messageByteArray.copyOf(),
            keyByteArray
        )
        if (areEquals(messageByteArray, encodedByteArray) == null) {
            Assert.fail("Byte array should not be equals")
        }
        val decodedByteArray = aesManager.decode(
            AesMode.ECB,
            encodedByteArray,
            keyByteArray
        )

        // Then
        for (i in 0 until messageByteArray.size) {
            if (messageByteArray[i] != decodedByteArray[i]) {
                Assert.fail("Byte diverges at index $i")
            }
        }
    }

    @Test
    fun encodeDecodeJson() {
        // Given
        val aesManager = AesModule().createAesManager()
        val message = "{\"key\": 42}"
        val messageByteArray = message.toByteArray()

        // When
        val encodedByteArray = aesManager.encode(
            AesMode.ECB,
            messageByteArray.copyOf(),
            keyByteArray
        )
        if (areEquals(messageByteArray, encodedByteArray) == null) {
            Assert.fail("Byte array should not be equals")
        }
        val decodedByteArray = aesManager.decode(
            AesMode.ECB,
            encodedByteArray,
            keyByteArray
        )

        // Then
        val decodedJson = String(trim(decodedByteArray))
        Assert.assertEquals(
            message,
            decodedJson
        )
    }

    private fun trim(bytes: ByteArray): ByteArray {
        var i = bytes.size - 1
        while (i >= 0 && bytes[i].toInt() == 0) {
            --i
        }
        return bytes.copyOf(i + 1)
    }

    /**
     * Null if equals
     */
    private fun areEquals(
        byteArrayReference: ByteArray,
        byteArray: ByteArray
    ): String? {
        if (byteArrayReference.size != byteArray.size) {
            return "${byteArrayReference.size} != ${byteArray.size}"
        }
        for (i in 0 until byteArrayReference.size) {
            if (byteArrayReference[i] != byteArray[i]) {
                return "Byte diverges at index $i"
            }
        }
        return null
    }

    private fun createByteArray(
        vararg ints: Int
    ) = ByteArray(ints.size) { pos -> ints[pos].toByte() }
}
