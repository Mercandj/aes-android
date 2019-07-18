package com.mercandalli.android.sdk.feature_aes_sample

import android.content.Context
import android.util.Log

import androidx.test.platform.app.InstrumentationRegistry

import com.mercandalli.android.sdk.feature_aes_native.AesNativeMode
import com.mercandalli.android.sdk.feature_aes_native.AesNativeModule
import com.mercandalli.sdk.feature_aes_java.AesJavaModule
import com.mercandalli.sdk.feature_aes_java.AesMode
import com.mercandalli.sdk.feature_aes_java.AesOpMode
import com.mercandalli.sdk.feature_aes_java.AesPadding
import org.junit.Assert

import org.junit.Test

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException

import javax.crypto.NoSuchPaddingException

class AesWorkflowAndroidTest {

    @Test
    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        IOException::class
    )
    fun workflow() {
        // Given
        val aesJavaManager = AesJavaModule().createAesJavaManager()
        val aesCrypter = aesJavaManager.getAesCrypter(
            AesOpMode.CRYPT,
            AesMode.ECB,
            AesPadding.NO,
            KeyContant.keyByteArray
        )
        val assetManager = getContext().assets
        val fileName = "soundcloud_tracks.mp3"
        val clearBytes = assetManager.open(fileName).readBytes()

        // When
        val inputStream = assetManager.open(fileName)
        var unclearFile: File? = null
        var fileOutputStream: FileOutputStream? = null
        var outputStream: OutputStream? = null
        try {
            unclearFile = File.createTempFile("tmp", ".mp3")
            unclearFile.deleteOnExit()
            val buffer = ByteArray(4096)

            fileOutputStream = FileOutputStream(unclearFile!!)
            if (aesCrypter == null) {
                outputStream = fileOutputStream
            } else {
                outputStream = aesCrypter.createCipherOutputStream(fileOutputStream)
            }
            var mod = 0
            var n: Int = inputStream.read(buffer)
            while (n != -1) {
                if (n > 0) {
                    outputStream!!.write(buffer, 0, n)
                }
                for (i in 0 until 4096 - n) {
                    outputStream!!.write(0)
                }
                mod = ++mod % 10
                n = inputStream.read(buffer)
            }
        } catch (e: IOException) {
            Log.e("DownloadCallback", "Download of crypt failed", e)
        } finally {
            try {
                fileOutputStream?.close()
            } catch (ignored: IOException) {
            }

            try {
                outputStream?.close()
            } catch (ignored: IOException) {
            }
        }
        val unclearByteArray = unclearFile!!.readBytes()

        val aesNativeManager = AesNativeModule().createAesNativeManager()
        val decodedByteArray = aesNativeManager.decode(
            AesNativeMode.ECB,
            unclearByteArray,
            KeyContant.keyByteArray
        )

        // Then
        Assert.assertNotNull(
            areEquals(
                clearBytes,
                unclearByteArray
            )
        )
        Assert.assertNull(
            areEquals(
                clearBytes,
                decodedByteArray,
                true
            )
        )
        // val outputClearFile = File(getContext().filesDir, "output_clear.mp3")
        // outputClearFile.deleteOnExit()
        // outputClearFile.writeBytes(clearBytes)
    }

    private fun getContext(): Context {
        return InstrumentationRegistry.getInstrumentation().targetContext
    }

    /**
     * Null if equals
     */
    private fun areEquals(
        byteArrayReference: ByteArray,
        byteArray: ByteArray,
        excludePadding: Boolean = false
    ): String? {
        if (!excludePadding) {
            if (byteArrayReference.size != byteArray.size) {
                return "${byteArrayReference.size} != ${byteArray.size}"
            }
        }
        for (i in 0 until byteArrayReference.size) {
            if (byteArrayReference[i] != byteArray[i]) {
                return "Byte diverges at index $i"
            }
        }
        return null
    }
}
