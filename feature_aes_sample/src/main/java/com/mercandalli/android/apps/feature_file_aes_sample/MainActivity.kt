package com.mercandalli.android.apps.feature_file_aes_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mercandalli.android.apps.feature_file_aes_sample.ActivityUtils.bind
import com.mercandalli.android.sdk.feature_aes.AesModule
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private val encode: View by bind(R.id.activity_main_encode)
    private val decode: View by bind(R.id.activity_main_decode)
    private val test: View by bind(R.id.activity_main_test)
    private val aesManager by lazy {
        AesModule().createAesManager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        encode.setOnClickListener {
            encode()
        }
        decode.setOnClickListener {
            decode()
        }
        test.setOnClickListener {
            test()
        }
    }


    private fun encode() {
        val output = aesManager.encode(
            "coucou",
            "key"
        )
        toast("Output: $output")
    }

    private fun decode() {
        val output = aesManager.decode(
            "coucou",
            "key"
        )
        toast("Output: $output")
    }

    private fun test() {
        val messageByteArray = byteArrayOfInts(
            0x6b,
            0xc1,
            0xbe,
            0xe2,
            0x2e,
            0x40,
            0x9f,
            0x96,
            0xe9,
            0x3d,
            0x7e,
            0x11,
            0x73,
            0x93,
            0x17,
            0x2a
        )
        val expectedOutputByteArray = byteArrayOfInts(
            0x3a,
            0xd7,
            0x7b,
            0xb4,
            0x0d,
            0x7a,
            0x36,
            0x60,
            0xa8,
            0x9e,
            0xca,
            0xf3,
            0x24,
            0x66,
            0xef,
            0x97
        )
        val keyByteArray = byteArrayOfInts(
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
        val output = aesManager.encode(
            messageByteArray,
            keyByteArray
        )
        if (expectedOutputByteArray.size != output.size) {
            toast("Oops, ${messageByteArray.size} != ${output.size}")
            val min = min(expectedOutputByteArray.size, output.size)
            for (i in 0 until min) {
                Log.d("jm/debug", "" + expectedOutputByteArray[i] + " : " + output[i])
            }
            return
        }
        for (i in 0 until expectedOutputByteArray.size) {
            Log.d("jm/debug", "" + expectedOutputByteArray[i] + " : " + output[i])
            if (expectedOutputByteArray[i] != output[i]) {
                toast("Oops, divergence on i: $i")
                return
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {

        fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }
    }
}
